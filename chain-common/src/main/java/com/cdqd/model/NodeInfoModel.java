package com.cdqd.model;

import com.cdqd.enums.StatusEnum;

import java.util.Date;

public class NodeInfoModel {
    private Integer id;

    private Integer nodeId;

    private String name;

    private String address;

    private Integer type;

    private String publicKey;

    private Date registerTime;

    private Integer status;

    private Integer blockIndex;

    public NodeInfoModel(Integer nodeId, String name, String publicKey, String address, Integer type) {
        this.nodeId = nodeId;
        this.name = name;
        this.address = address;
        this.type = type;
        this.publicKey = publicKey;
        this.registerTime = new Date();
        this.blockIndex = 0;
        this.status = StatusEnum.ON.getCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey == null ? null : publicKey.trim();
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(Integer blockIndex) {
        this.blockIndex = blockIndex;
    }
}