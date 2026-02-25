package com.graduation.hospital.service;

import com.graduation.hospital.entity.SysUser;
import com.graduation.hospital.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SysUser register(SysUser user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        SysUser saved = userRepository.save(user);
        log.info("用户注册成功: username={}", user.getUsername());
        return saved;
    }

    @Override
    public SysUser login(String username, String password) {
        SysUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }
        if (!user.getEnabled()) {
            throw new IllegalArgumentException("用户已被禁用");
        }
        log.info("用户登录成功: username={}", username);
        return user;
    }

    @Override
    public SysUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    @Override
    public SysUser getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    @Override
    public List<SysUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public SysUser updateUser(Long id, SysUser user) {
        SysUser existing = getUserById(id);
        if (user.getEmail() != null) {
            existing.setEmail(user.getEmail());
        }
        SysUser updated = userRepository.save(existing);
        log.info("更新用户信息成功: id={}", id);
        return updated;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("用户不存在");
        }
        userRepository.deleteById(id);
        log.info("删除用户成功: id={}", id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        SysUser user = getUserById(id);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("修改密码成功: userId={}", id);
    }

    @Override
    @Transactional
    public void enableUser(Long id) {
        SysUser user = getUserById(id);
        user.setEnabled(true);
        userRepository.save(user);
        log.info("启用用户: id={}", id);
    }

    @Override
    @Transactional
    public void disableUser(Long id) {
        SysUser user = getUserById(id);
        user.setEnabled(false);
        userRepository.save(user);
        log.info("禁用用户: id={}", id);
    }
}