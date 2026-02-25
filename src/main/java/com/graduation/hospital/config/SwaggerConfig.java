package com.graduation.hospital.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger API 文档配置
 * 支持多版本 API 文档展示
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HospitalInfoManagement API")
                        .version("v26.2.1")
                        .description("""
                                社区医院病人信息管理系统 REST API 文档

                                ## API 版本说明
                                - **v1**: 默认版本，生产环境使用
                                - **v2**: 新功能版本，包含测试中的 API

                                ## 认证说明
                                使用 JWT Token 认证，在请求头中添加：
                                `Authorization: Bearer <token>`
                                """)
                        .contact(new Contact()
                                .name("Hospital 开发组")
                                .email("laugh0608@foxmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}