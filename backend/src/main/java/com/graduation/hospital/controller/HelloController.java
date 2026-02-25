package com.graduation.hospital.controller;

import com.graduation.hospital.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Hello World 测试控制器
 * 提供 API 版本测试接口
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    /**
     * v1 版本 - 默认版本，返回基础问候信息
     */
    @GetMapping("/v1/hello")
    public Result<String> helloV1() {
        return Result.success("Hello World from Hospital Management System v1!");
    }

    /**
     * v2 版本 - 新功能版本，返回更多元信息
     */
    @GetMapping("/v2/hello")
    public Result<Map<String, Object>> helloV2() {
        return Result.success(Map.of(
            "message", "Hello World from Hospital Management System v2!",
            "version", "v2",
            "timestamp", LocalDateTime.now().toString(),
            "features", new String[]{"enhanced-response", "timestamp", "version-info"}
        ));
    }

    /**
     * 兼容旧版 /api/hello（重定向到 v1）
     */
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello World from Hospital Management System v1!");
    }
}
