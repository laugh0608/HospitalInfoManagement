package com.graduation.hospital.config;

import com.graduation.hospital.entity.SysRole;
import com.graduation.hospital.entity.SysUser;
import com.graduation.hospital.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义 UserDetailsService
 * 用于 Spring Security 加载用户信息
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        // 构建权限列表
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (user.getRoles() != null) {
            for (SysRole role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
            }
        }

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!user.getEnabled())
                .build();
    }
}