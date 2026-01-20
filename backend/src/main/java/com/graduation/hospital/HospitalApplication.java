package com.graduation.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;

/**
 * 医院信息管理系统主应用
 * 开发阶段禁用 Redis 自动配置
 */
@SpringBootApplication(exclude = {
    RedisAutoConfiguration.class,
    RedisReactiveAutoConfiguration.class
})
public class HospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }

}
