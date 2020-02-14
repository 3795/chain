package com.cdqd.service.impl;

import com.cdqd.core.Block;
import com.cdqd.core.BlockContent;
import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.exception.ServerException;
import com.cdqd.mapper.BlockContentModelMapper;
import com.cdqd.mapper.BlockModelMapper;
import com.cdqd.model.BlockContentModel;
import com.cdqd.model.BlockContentModelExample;
import com.cdqd.model.BlockModel;
import com.cdqd.model.BlockModelExample;
import com.cdqd.service.BlockService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
            BlockContentModel contentModel = new BlockContentModel(block.getIndex(), content.getContent());
            insert(contentModel);
        }
        logger.info("区块写入成功, 区块索引: {}", block.getIndex());
    }

    @Override
    public Block queryPrevBlock() {
        BlockModelExample example = new BlockModelExample();
        example.setOrderByClause("block_index desc");
        List<BlockModel> blockModelList = blockMapper.selectByExample(example);
        if (blockModelList.size() == 0) {
            throw new ServerException(ResponseCodeEnum.EMPTY_CHAIN);
        }
        return Block.blockModel2Block(blockModelList.get(0), null);
    }

    @Override
    public List<Block> pullBlock(Integer blockIndex, Integer size) {
        PageHelper.startPage(1, size);
        BlockModelExample example = new BlockModelExample();
        BlockModelExample.Criteria criteria = example.createCriteria();
        criteria.andBlockIndexGreaterThanOrEqualTo(blockIndex);
        example.setOrderByClause("block_index asc");
        List<BlockModel> blockModelList = blockMapper.selectByExample(example);
        List<Block> blockList = new ArrayList<>();
        for (BlockModel model : blockModelList) {
            List<BlockContent> contentList = queryContentByBlockIndex(model.getBlockIndex());
            blockList.add(Block.blockModel2Block(model, contentList));
        }
        return blockList;
    }

    /**
     * 查询区块中保存的内容
     *
     * @param blockIndex
     * @return
     */
    private List<BlockContent> queryContentByBlockIndex(Integer blockIndex) {
        BlockContentModelExample example = new BlockContentModelExample();
        BlockContentModelExample.Criteria criteria = example.createCriteria();
        criteria.andBlockIndexEqualTo(blockIndex);
        List<BlockContentModel> blockContentModelList = contentMapper.selectByExample(example);
        List<BlockContent> blockContentList = new ArrayList<>();
        for (BlockContentModel model : blockContentModelList) {
            BlockContent content = new BlockContent(model.getContent());
            blockContentList.add(content);
        }
        return blockContentList;
    }

    /**
     * 插入记录
     *
     * @param blockModel
     */
    private void insert(BlockModel blockModel) {
        int result = blockMapper.insertSelective(blockModel);
        if (result != 1) {
            throw new ServerException(ResponseCodeEnum.INSERT_FAILED, "Insert into block failed");
        }
    }

    /**
     * 插入记录
     *
     * @param contentModel
     */
    private void insert(BlockContentModel contentModel) {
        int result = contentMapper.insertSelective(contentModel);
        if (result != 1) {
            throw new ServerException(ResponseCodeEnum.INSERT_FAILED, "Insert into block_content failed");
        }
    }
}
