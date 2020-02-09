package com.cdqd.core;

import com.alibaba.fastjson.JSON;
import com.cdqd.util.EncryptUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public Block(int index, List<BlockContent> contentList, String prevHashValue, Date createTime) {
        this.index = index;
        this.contentList = contentList;
        this.prevHashValue = prevHashValue;
        this.createTime = createTime;
        this.hashValue = sumHashValue(this);
    }

    /**
     * 计算区块的Hash值
     * @param block
     * @return
     */
    private static String sumHashValue(Block block) {
        StringBuilder buffer = new StringBuilder();
        for (BlockContent content : block.contentList) {
            buffer.append(JSON.toJSONString(content)).append(",");
        }
        String content = String.format("Index:%d, ContentList:%s, PrevHashValue:%s, CreateTime:%s",
                block.index, buffer.toString(), block.prevHashValue, block.createTime);
        return EncryptUtil.md5(content);
    }

    /**
     * 生成区块链系统中的第一个区块
     * @return
     */
    public static Block generateInitialBlock() {
        return new Block(1, new BlockContent("Initial Block Content"), "");
    }

    public String getHashValue() {
        return hashValue;
    }


}
