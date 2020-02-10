package com.cdqd.model;

public class BlockContentModel {
    private Integer id;

    private Integer blockIndex;

    private String content;

    public BlockContentModel(Integer blockIndex, String content) {
        this.blockIndex = blockIndex;
        this.content = content;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}