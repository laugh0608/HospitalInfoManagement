package com.graduation.hospital.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 配置类，启用 JPA 审计功能
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}