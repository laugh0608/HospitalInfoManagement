package com.graduation.hospital.service;

import com.graduation.hospital.entity.SysUser;

import java.util.List;

public interface SysUserService {

    SysUser register(SysUser user);

    SysUser login(String username, String password);

    SysUser getUserById(Long id);

    SysUser getUserByUsername(String username);

    List<SysUser> getAllUsers();

    SysUser updateUser(Long id, SysUser user);

    void deleteUser(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void changePassword(Long id, String oldPassword, String newPassword);

    void enableUser(Long id);

    void disableUser(Long id);
}