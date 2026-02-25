package com.graduation.hospital.repository;

import com.graduation.hospital.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 系统用户数据访问仓储
 * 提供用户相关的数据库操作
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    /**
     * 根据用户名查询用户（用于登录验证）
     * @param username 用户名
     */
    Optional<SysUser> findByUsername(String username);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
}