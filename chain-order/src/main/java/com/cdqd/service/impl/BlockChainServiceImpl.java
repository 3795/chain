package com.cdqd.service.impl;

import com.cdqd.core.Block;
import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.exception.ServerException;
import com.cdqd.service.AsyncService;
import com.cdqd.service.BlockChainService;
import com.cdqd.service.BlockService;
import com.cdqd.service.NetworkService;
import com.cdqd.util.BlockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.cdqd.data.MetaData.chainData;
import static com.cdqd.data.MetaData.orderData;

/**
 * Description: 节点使用的区块链服务
 * Created At 2020/2/10
 */
@Service
public class BlockChainServiceImpl implements BlockChainService {

    private final static Logger logger = LoggerFactory.getLogger(BlockServiceImpl.class);

    @Autowired
    private BlockService blockService;

    @Autowired
    private NetworkService networkService;

    @Autowired
    private AsyncService asyncService;

    private static volatile int successCount = 0;      // 广播成功的次数

    private static volatile int addressCount = 0;      // 需要广播的地址总数

    @Override
    public boolean chainExist() {
        return blockService.exist();
    }

    @Override
    public synchronized void insertBlock(Block block) {
        if (!BlockUtil.verify(chainData.getIndex() + 1, chainData.getPrevHashValue(), block)) {
            throw new ServerException(ResponseCodeEnum.INSERT_BLOCK_FAILED);
        }
        blockService.insertBlock(block);
        chainData.update(block.getHashValue());
    }

    @Override
    public Block queryPrevBlock() {
        return blockService.queryPrevBlock();
    }

    @Override
    public List<Block> pullBlock(Integer blockIndex, Integer size) {
        return blockService.pullBlock(blockIndex, size);
    }

    @Override
    public synchronized void syncBlock(String targetAddress) {
        // 查询目标节点的区块高度
        int remoteBlockIndex = networkService.getBlockIndex(targetAddress);
        int localBlockIndex = chainData.getIndex();
        if (remoteBlockIndex <= localBlockIndex) {
            return;
        }
        logger.info("开始同步区块");
        int count = 0;
        long startTime = System.currentTimeMillis();
        int startIndex = localBlockIndex + 1;
        while (startIndex <= remoteBlockIndex) {
            List<Block> blockList = networkService.pullBlock(targetAddress, startIndex, 10);
            for (Block block : blockList) {
                insertBlock(block);
                startIndex += 1;
                count++;
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("区块同步结束，耗时: {} ms, 增加区块数量: {}块", endTime - startTime, count);
    }

    /**
     * 并发的对其他Order节点进行广播
     * 其他Order节点收到广播的区块后，先放入临时区域，并响应
     * Leader节点收到大于n/2的节点响应后，广播一个写入区块的ack消息，其他Order节点收到ack消息后，将临时区域的区块写入存储系统
     * Leader节点将区块写入自己的存储系统
     * 流程中，如果leader意外死亡，因为消息没有ack，则消息还是会被下一个leader消费掉，不会发生消息丢失
     * 如果本次广播过程中，出现失败的情况，则放弃后续所有流程，消息未被消费，直接进入到下一次循环
     *
     * @param messageList
     * @return
     */
    @Override
    public boolean packAndBroadcast(List<String> messageList) {
        Block block = Block.generateBlock(chainData, messageList);
        long startTime = System.currentTimeMillis();
        if (broadcast(block)) {
            // 发送确认写入的广播
            for (Map.Entry<Integer, String> entry : orderData.getAvailableOrder().entrySet()) {
                boolean result = networkService.broadcastAckBlock(entry.getValue());
                if (!result) {
                    orderData.addDoubtOrder(entry.getKey());
                }
            }
            // 本节点写入区块
            insertBlock(block);
            long endTime = System.currentTimeMillis();
            logger.info("本次广播耗时：{} ms", endTime - startTime);
            return true;
        }
        return false;
    }

    @Override
    public boolean broadcast(Block block) {
        successCount = 0;
        addressCount = 0;
        for (Map.Entry<Integer, String> entry : orderData.getAvailableOrder().entrySet()) {
            asyncService.broadcastBlock(entry.getValue(), block);
        }
        while (true) {
            if (addressCount >= orderData.getAvailableOrder().size()) {
                break;
            } else {
                try {
                    // 睡眠500ms
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("广播过程完成，successCount: {}, addressCount: {}", successCount, addressCount);
        return successCount != 0 && 2 * successCount >= addressCount;
    }

    /**
     * 安全增加广播成功次数
     */
    synchronized static void addSuccessCount() {
        successCount++;
    }

    /**
     * 广播的次数+1
     */
    synchronized static void addAddressCount() {
        addressCount++;
    }


}
