package com.cdqd.dto;

import java.util.Date;
import java.util.List;

/**
 * Description: 区块数据结构
 * Created At 2020/2/9
 */
public class BlockDTO {

    private int index;     // 索引值（区块高度）

    private List<BlockContentDTO> contentList;     // 区块内容(1条或n条数据)

    private String hashValue;       // 本区块的Hash值

    private String prevHashValue;       // 上一个区块的Hash值

    private Date createTime;        // 区块创建时间

    public BlockDTO() {
    }

    public String getHashValue() {
        return hashValue;
    }

    public List<BlockContentDTO> getContentList() {
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

    public void setIndex(int index) {
        this.index = index;
    }

    public void setContentList(List<BlockContentDTO> contentList) {
        this.contentList = contentList;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public void setPrevHashValue(String prevHashValue) {
        this.prevHashValue = prevHashValue;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
