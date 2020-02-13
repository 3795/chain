package com.cdqd.service.impl;

import com.cdqd.core.Block;
import com.cdqd.data.MetaData;
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
import static com.cdqd.data.MetaData.peerData;

/**
 * Description: 对Peer节点中的区块链进行操作
 * Created At 2020/2/13
 */
@Service
public class BlockChainServiceImpl implements BlockChainService {

    private static final Logger logger = LoggerFactory.getLogger(BlockChainServiceImpl.class);

    private int size = 10;

    @Autowired
    private BlockService blockService;

    @Autowired
    private NetworkService networkService;

    @Override
    public boolean chainExist() {
        return blockService.exist();
    }

    @Override
    public Block queryPrevBlock() {
        return blockService.queryPrevBlock();
    }

    @Override
    public void syncBlock() {
        for (Map.Entry<Integer, String> entry : peerData.getAvailableOrder().entrySet()) {
            try {
                doSync(entry.getValue());
            } catch (Exception e) {

            }
        }
    }

    @Override
    public synchronized void insertBlock(Block block) {
        if (!BlockUtil.verify(chainData.getIndex() + 1, chainData.getPrevHashValue(), block)) {
            throw new ServerException(ResponseCodeEnum.INSERT_BLOCK_FAILED);
        }
        blockService.insertBlock(block);
        chainData.update(block.getHashValue());
    }

    private void doSync(String address) {
        long startTime = System.currentTimeMillis();
        int localBlockIndex = chainData.getIndex();
        int remoteBlockIndex = networkService.getBlockIndex(address);
        if (remoteBlockIndex <= localBlockIndex) {
            return;
        }
        logger.info("开始从节点 {} 处同步区块", address);
        int count = 0;
        int startIndex = localBlockIndex + 1;
        while (startIndex <= remoteBlockIndex) {
            List<Block> blockList = networkService.pullBlock(address, startIndex, size);
            for (Block block : blockList) {
                insertBlock(block);
                startIndex += 1;
                count ++;
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("区块同步结束，耗时: {} ms, 增加区块数量: {}块", endTime - startTime, count);
    }
}
