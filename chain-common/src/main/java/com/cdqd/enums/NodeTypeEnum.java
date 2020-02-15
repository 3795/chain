package com.cdqd.enums;

/**
 * 节点类型枚举
 */
public enum NodeTypeEnum {

    ORDER(1, "Order"),

    PEER(2, "Peer"),
    ;

    private Integer type;

    private String desc;

    NodeTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
