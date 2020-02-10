package com.cdqd.core;

import com.cdqd.dto.BlockContentDTO;
import com.cdqd.dto.BlockDTO;
import com.cdqd.model.BlockModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cdqd.util.BlockUtil.sumHashValue;
//import static com.cdqd.

/**
 * Description: 区块数据结构
 * Created At 2020/2/9
 */
public class Block {

    private int index;     // 索引值（区块高度）

    private List<BlockContent> contentList;     // 区块内容(1条或n条数据)

    private String hashValue;       // 本区块的Hash值

    private String prevHashValue;       // 上一个区块的Hash值

    private Date createTime;        // 区块创建时间

    public Block(int index, BlockContent blockContent, String prevHashValue) {
        this.index = index;
        this.contentList = new ArrayList<>();
        this.contentList.add(blockContent);
        this.prevHashValue = prevHashValue;
        this.createTime = new Date();
        this.hashValue = sumHashValue(this);
    }

    public Block(int index, List<String> contents, String prevHashValue) {
        this.index = index;
        this.contentList = new ArrayList<>();
        for (String content : contents) {
            this.contentList.add(new BlockContent(content));
        }
        this.prevHashValue = prevHashValue;
        this.createTime = new Date();
        this.hashValue = sumHashValue(this);
    }

    /**
     * 根据数据模型构造区块
     *
     * @param blockModel
     * @param contentList
     */
    public Block(BlockModel blockModel, List<BlockContent> contentList) {
        this.index = blockModel.getBlockIndex();
        this.hashValue = blockModel.getHashValue();
        this.prevHashValue = blockModel.getPrevHashValue();
        this.createTime = blockModel.getCreateTime();
        this.contentList = contentList;
    }

    /**
     * 根据数据模型构造区块
     *
     * @param blockModel
     */
    public Block(BlockModel blockModel) {
        this.index = blockModel.getBlockIndex();
        this.hashValue = blockModel.getHashValue();
        this.prevHashValue = blockModel.getPrevHashValue();
        this.createTime = blockModel.getCreateTime();
    }

    public Block(BlockDTO blockDTO) {
        this.index = blockDTO.getIndex();
        this.hashValue = blockDTO.getHashValue();
        this.prevHashValue = blockDTO.getPrevHashValue();
        this.createTime = blockDTO.getCreateTime();
        this.contentList = new ArrayList<>();
        for (BlockContentDTO dto : blockDTO.getContentList()) {
            this.contentList.add(new BlockContent(dto.getContent()));
        }
    }

    /**
     * 生成区块链系统中的第一个区块
     *
     * @return
     */
    public static Block generateInitialBlock() {
        return new Block(1, new BlockContent("Initial Block Content"), "0");
    }

    public String getHashValue() {
        return hashValue;
    }

    public List<BlockContent> getContentList() {
        return contentList;
    }

    public int getIndex() {
        return index;
    }

    public String getPrevHashValue() {
        return prevHashValue;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
