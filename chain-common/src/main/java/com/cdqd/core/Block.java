package com.cdqd.core;

import com.cdqd.dto.BlockContentDTO;
import com.cdqd.dto.BlockDTO;
import com.cdqd.model.BlockModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.cdqd.util.BlockUtil.sumHashValue;

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

    private Block(int index, List<String> contents, String prevHashValue) {
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
     * 模型转换
     *
     * @param blockModel
     * @param contentList
     * @return
     */
    public static Block blockModel2Block(BlockModel blockModel, List<BlockContent> contentList) {
        return new Block(blockModel, contentList);
    }

    /**
     * 模型转换
     * @param blockDTO
     * @return
     */
    public static Block blockDTO2Block(BlockDTO blockDTO) {
        return new Block(blockDTO);
    }

    /**
     * 生成区块链系统中的第一个区块
     *
     * @return
     */
    public static Block generateInitialBlock() {
        return new Block(1, Collections.singletonList("Initial Block Content"), "0");
    }

    /**
     * 生成一个新区块
     *
     * @param chainData
     * @param contents
     * @return
     */
    public static Block generateBlock(ChainData chainData, List<String> contents) {
        return new Block(chainData.getIndex() + 1, contents, chainData.getPrevHashValue());
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

    private Block(BlockModel blockModel, List<BlockContent> contentList) {
        this.index = blockModel.getBlockIndex();
        this.hashValue = blockModel.getHashValue();
        this.prevHashValue = blockModel.getPrevHashValue();
        this.createTime = blockModel.getCreateTime();
        this.contentList = contentList;
    }

    private Block(BlockDTO blockDTO) {
        this.index = blockDTO.getIndex();
        this.hashValue = blockDTO.getHashValue();
        this.prevHashValue = blockDTO.getPrevHashValue();
        this.createTime = blockDTO.getCreateTime();
        this.contentList = new ArrayList<>();
        for (BlockContentDTO dto : blockDTO.getContentList()) {
            this.contentList.add(new BlockContent(dto.getContent()));
        }
    }
}
