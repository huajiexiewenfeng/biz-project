package com.csdn.biz.api.enums;

import org.springframework.http.HttpStatus;

/**
 * 状态码
 *
 * @Author: xiewenfeng
 * @Date: 2022/11/14 17:54
 * @see HttpStatus
 */
public enum StatusCode {
    OK(0, "OK") {
        @Override
        public String getMessage() {
            return super.message;
        }
    },
    FAILED(-1, "Failed"),
    CONTINUE(1, "{status-code.continue}");

    private int code;

    private String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return getLocalizedMessage();
    }

    public String getLocalizedMessage() {
        // FIXME 增加国际化支持
        // 如果 message 是占位符，翻译成档案的 message text
        // 否则直接返回
        return message;
    }
}
