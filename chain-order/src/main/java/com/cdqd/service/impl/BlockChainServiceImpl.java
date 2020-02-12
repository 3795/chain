package com.cdqd.service.impl;

import com.cdqd.core.Block;
import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.exception.ServerException;
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

    private final static Logger logger = LoggerFactory.getLogger(BlockChainServiceImpl.class);

    @Autowired
    private BlockService blockService;

    @Autowired
    private NetworkService networkService;

    @Override
    public boolean chainExist() {
        return blockService.exist();
    }

    @Override
    public void insertBlock(Block block) {
        if (!BlockUtil.verify(chainData.getIndex() + 1, chainData.getPrevHashValue(), block)) {
            throw new ServerException(ResponseCodeEnum.ERROR.getCode(), "区块校验失败，追加区块取消");
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
    public void syncBlock(Integer startIndex, Integer endIndex) {
        logger.info("开始同步区块");
        int count = 0;
        long startTime = System.currentTimeMillis();
        while (startIndex <= endIndex) {
            List<Block> blockList = networkService.pullBlockFromLeader(startIndex, 10);
            for (Block block : blockList) {
                insertBlock(block);
                startIndex += 1;
                count ++;
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("区块同步结束，耗时: {} ms, 增加区块数量: {}块", endTime - startTime, count);
    }

    /**
     *  并发的对其他Order节点进行广播
     *  其他Order节点收到广播的区块后，先放入临时区域，并响应
     *  Leader节点收到大于n/2的节点响应后，广播一个写入区块的ack消息，其他Order节点收到ack消息后，将临时区域的区块写入存储系统
     *  Leader节点将区块写入自己的存储系统
     *  流程中，如果leader意外死亡，因为消息没有ack，则消息还是会被下一个leader消费掉，不会发生消息丢失
     *  如果本次广播过程中，出现失败的情况，则放弃后续所有流程，消息未被消费，直接进入到下一次循环
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
                    orderData.addDoubtNode(entry.getKey());
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
        int count = 0;
        int total = 0;
        orderData.getOrderAddressMap().size();
        // 这个地方比较耗时，后续用并发优化
        for (Map.Entry<Integer, String> entry : orderData.getAvailableOrder().entrySet()) {
            boolean result = networkService.broadcastBlock(entry.getValue(), block);
            if (result) {
                count ++;
            } else {
                // 发送失败，则将该节点信息加入怀疑节点，确认节点是否存活
                orderData.addDoubtNode(entry.getKey());
            }
            total ++;
        }
        return count != 0 && 2 * count >= total;
    }


}
