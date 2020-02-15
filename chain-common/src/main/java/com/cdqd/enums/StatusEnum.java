package com.cdqd.enums;

/**
 * 状态枚举
 */
public enum StatusEnum {

    ON(1, "开启"),

    OFF(0, "关闭"),
    ;

    private Integer code;

    private String desc;

    StatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
