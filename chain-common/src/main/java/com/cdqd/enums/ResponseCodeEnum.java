package com.cdqd.enums;

/**
 * 系统返回值枚举
 */
public enum ResponseCodeEnum {
    SUCCESS(0, "SUCCESS"),

    ERROR(10, "ERROR"),
    REMOTE_CALL_FAIL(11, "Remote Call Failed"),
    INSERT_FAILED(12, "插入记录失败"),
    UPDATE_FAILED(13, "更新记录失败"),
    DELETE_FAILED(14, "删除记录失败"),
    NOT_FOUND(15, "资源不存在"),
    UNAUTHORIZED(16, "权限不足"),
    SERVER_INTERNAL_ERROR(17, "应用出了小问题，请稍后再试"),
    ILLEGAL_PARAMETER(18, "参数非法"),
    FILE_UPLOAD_FAILED(19, "文件上传失败"),

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
