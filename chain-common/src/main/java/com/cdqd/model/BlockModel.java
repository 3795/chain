package com.cdqd.model;

import java.util.Date;

public class BlockModel {
    private Integer id;

    private Integer blockIndex;

    private String hashValue;

    private String prevHashValue;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(Integer blockIndex) {
        this.blockIndex = blockIndex;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue == null ? null : hashValue.trim();
    }

    public String getPrevHashValue() {
        return prevHashValue;
    }

    public void setPrevHashValue(String prevHashValue) {
        this.prevHashValue = prevHashValue == null ? null : prevHashValue.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}