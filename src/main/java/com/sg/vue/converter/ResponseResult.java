package com.sg.vue.converter;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {
    private String code;
    private String message;
    private transient T data;
    private Long total;
    private String traceID;

    public ResponseResult() {
    }

    public ResponseResult(String code, String message, T data, Long total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
    }

    public ResponseResult(String code, String message, T data) {
        this(code, message, data, null);
    }

    public ResponseResult(String code, String message) {
        this(code, message, null, null);
    }

    public ResponseResult(T data) {
        this("00000", "success", data, null);
    }

    public ResponseResult(T data, Long total) {
        this("00000", "success", data, total);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getTraceID() {
        return traceID;
    }

    public void setTraceID(String traceID) {
        this.traceID = traceID;
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(data);
    }

    public static <T> ResponseResult<T> success(T data, Long total) {
        return new ResponseResult<>(data, total);
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(null);
    }

    public static <T> ResponseResult<T> fail(String code, String message, T data) {
        return new ResponseResult<>(code, message, data);
    }

    public static <T> ResponseResult<T> fail(String code, String message) {
        return new ResponseResult<>(code, message);
    }
}
