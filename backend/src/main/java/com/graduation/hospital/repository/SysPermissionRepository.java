package com.graduation.hospital.repository;

import com.graduation.hospital.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {

    Optional<SysPermission> findByCode(String code);

    boolean existsByCode(String code);
}