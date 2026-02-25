package com.graduation.hospital.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 目录初始化器
 * 应用启动时自动创建必要的目录
 */
@Slf4j
@Component
public class DirectoryInitializer {

    private static final String[] REQUIRED_DIRECTORIES = {
        "db",    // Database directory
        "logs"   // Log directory
    };

    @PostConstruct
    public void init() {
        for (String dir : REQUIRED_DIRECTORIES) {
            createDirectoryIfNotExists(dir);
        }
    }

    /**
     * 如果目录不存在则创建
     */
    private void createDirectoryIfNotExists(String dirName) {
        try {
            Path path = Paths.get(dirName);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("Created directory: {}", dirName);
            }
        } catch (Exception e) {
            log.warn("Failed to create directory {}: {}", dirName, e.getMessage());
        }
    }
}