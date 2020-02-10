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

import static com.cdqd.data.MetaData.chainData;

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


}
