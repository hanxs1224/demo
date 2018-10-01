package com.example.common.ajax;


public enum CallResult {
    FAILURE(0, "调用失败"),
    SUCCESS(1, "调用成功"),
    TOKEN_INVALID(401, "token失效，请重新登录");

    private final Integer code;

    private final String value;

    CallResult(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
