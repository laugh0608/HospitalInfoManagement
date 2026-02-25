package com.graduation.hospital.common.log.db;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库日志配置
 */
@Configuration
@ConditionalOnProperty(name = "logging.db.enabled", havingValue = "true")
public class DbLoggingConfig {

    /**
     * 配置 Hibernate 使用 SQL 日志拦截器
     * 注意：需要在 application.properties 中添加：
     * spring.jpa.properties.hibernate.session_factory.statement_inspector=com.graduation.hospital.common.log.db.SqlLoggingInterceptor
     */
}