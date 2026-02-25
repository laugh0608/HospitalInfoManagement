package com.graduation.hospital.common;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * 统一 API 响应封装
 * 所有 REST API 返回值使用此格式
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码（200=成功，其他参考错误码规范） */
    private int code;

    /** 提示信息 */
    private String message;

    /** 响应数据 */
    private T data;

    /** 时间戳（Unix 秒） */
    private long timestamp;

    public Result() {
        this.timestamp = Instant.now().getEpochSecond();
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = Instant.now().getEpochSecond();
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().getEpochSecond();
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "success");
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }

    public boolean isSuccess() {
        return this.code == 200;
    }
}