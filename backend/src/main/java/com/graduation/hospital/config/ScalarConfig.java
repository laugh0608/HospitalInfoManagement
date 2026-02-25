package com.graduation.hospital.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Scalar (Swagger) API 文档配置
 */
@Configuration
public class ScalarConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HospitalInfoManagement API")
                        .version("v26.2.1")
                        .description("社区医院病人信息管理系统 REST API 文档")
                        .contact(new Contact()
                                .name("Hospital 开发组")
                                .email("laugh0608@foxmail.com")));
    }
}