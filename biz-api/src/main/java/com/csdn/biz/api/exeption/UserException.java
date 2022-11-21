package com.csdn.biz.api.exeption;

/**
 * @author ：xwf
 * @date ：Created in 2022\11\21 0021 12:14
 */
public class UserException extends RuntimeException {

    public UserException() {
    }

    public UserException(String message, Throwable cause) {
        // 不抛出堆栈信息，减少性能消耗
        super(message, cause, false, false);
    }
}
