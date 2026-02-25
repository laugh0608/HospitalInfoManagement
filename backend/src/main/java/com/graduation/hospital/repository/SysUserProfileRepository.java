package com.graduation.hospital.repository;

import com.graduation.hospital.entity.SysUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserProfileRepository extends JpaRepository<SysUserProfile, Long> {

    Optional<SysUserProfile> findByUserId(Long userId);
}