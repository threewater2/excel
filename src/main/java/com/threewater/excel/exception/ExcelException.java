package com.threewater.excel.exception;

/**
 * 读取excel或者解析excel时的错误
 */
public class ExcelException extends Exception {
    public ExcelException(String message) {
        super(message);
    }
}
