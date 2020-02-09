package com.cdqd.service.impl;

import com.cdqd.core.Block;
import com.cdqd.core.BlockContent;
import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.exception.ServerException;
import com.cdqd.mapper.BlockContentModelMapper;
import com.cdqd.mapper.BlockModelMapper;
import com.cdqd.model.BlockContentModel;
import com.cdqd.model.BlockModel;
import com.cdqd.model.BlockModelExample;
import com.cdqd.service.BlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * Created At 2020/2/9
 */
@Service
public class BlockServiceImpl implements BlockService {

    private final static Logger logger = LoggerFactory.getLogger(BlockServiceImpl.class);

    @Autowired
    private BlockModelMapper blockMapper;

    @Autowired
    private BlockContentModelMapper contentMapper;

    @Override
    public boolean exist() {
        BlockModelExample example = new BlockModelExample();
        return blockMapper.countByExample(example) != 0;
    }

    @Override
    @Transactional
    public void insertBlock(Block block) {
        BlockModel blockModel = new BlockModel();
        BeanUtils.copyProperties(block, blockModel);
        blockModel.setBlockIndex(block.getIndex());
        insert(blockModel);
        for (BlockContent content : block.getContentList()) {
            BlockContentModel contentModel = new BlockContentModel(block.getIndex(), content);
            insert(contentModel);
        }
        logger.info("追加区块成功, 区块索引{}", block.getIndex());
    }

    private void insert(BlockModel blockModel) {
        int result = blockMapper.insertSelective(blockModel);
        if (result != 1) {
            throw new ServerException("Insert into block failed", ResponseCodeEnum.INSERT_FAILED);
        }
    }

    private void insert(BlockContentModel contentModel) {
        int result = contentMapper.insertSelective(contentModel);
        if (result != 1) {
            throw new ServerException("Insert into block_content failed", ResponseCodeEnum.INSERT_FAILED);
        }
    }
}
