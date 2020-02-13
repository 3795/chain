package com.cdqd.enums;

/**
 * 系统返回值枚举
 */
public enum ResponseCodeEnum {
    SUCCESS(0, "SUCCESS"),

    ERROR(10, "ERROR"),
    REMOTE_CALL_FAILED(11, "Remote Call Failed"),
    INSERT_FAILED(12, "插入记录失败"),
    EMPTY_CHAIN(13, "区块链为空"),
    IGNORE_BROAD_BLOCK(14, "忽略广播收到的区块"),
    INSERT_BLOCK_FAILED(15, "区块校验失败，追加区块取消"),

    ;
    private int code;

    private String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
