package com.graduation.hospital.common;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * 统一 API 响应封装
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;
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