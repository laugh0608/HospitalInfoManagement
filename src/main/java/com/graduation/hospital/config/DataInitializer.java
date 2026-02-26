package com.graduation.hospital.config;

import com.graduation.hospital.entity.SysRole;
import com.graduation.hospital.entity.SysUser;
import com.graduation.hospital.repository.SysRoleRepository;
import com.graduation.hospital.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 数据初始化器
 * 应用启动时自动检测并初始化种子数据（角色 + 管理员账户）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SysRoleRepository sysRoleRepository;
    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        initRoles();
        initAdminUser();
    }

    /** 初始化内置角色 */
    private void initRoles() {
        createRoleIfNotExists("ADMIN", "管理员", "拥有所有权限");
        createRoleIfNotExists("DOCTOR", "医生", "病人、病历、预约管理");
        createRoleIfNotExists("NURSE", "护士", "病人、预约、药品管理");
        createRoleIfNotExists("PATIENT", "患者", "预约挂号、查看自己病历");
    }

    private void createRoleIfNotExists(String code, String name, String description) {
        if (sysRoleRepository.existsByCode(code)) {
            return;
        }
        SysRole role = new SysRole();
        role.setCode(code);
        role.setName(name);
        role.setDescription(description);
        sysRoleRepository.save(role);
        log.info("初始化角色: {} ({})", name, code);
    }

    /** 初始化管理员账户 */
    private void initAdminUser() {
        if (sysUserRepository.existsByUsername("admin")) {
            return;
        }
        SysRole adminRole = sysRoleRepository.findByCode("ADMIN")
                .orElseThrow(() -> new IllegalStateException("ADMIN 角色不存在，无法创建管理员账户"));

        SysUser admin = new SysUser();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123456"));
        admin.setEnabled(true);
        admin.setRoles(Set.of(adminRole));
        sysUserRepository.save(admin);
        log.info("初始化管理员账户: admin");
    }
}
