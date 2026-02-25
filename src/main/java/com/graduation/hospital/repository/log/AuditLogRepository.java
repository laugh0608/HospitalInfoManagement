package com.graduation.hospital.repository.log;

import com.graduation.hospital.entity.log.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 审计日志数据访问仓储
 * 注意：实际表名为分表名称（audit_YYYYMMDD）
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    /**
     * 根据时间范围查询
     */
    List<AuditLog> findByLogTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据用户名查询
     */
    List<AuditLog> findByUsername(String username);

    /**
     * 根据操作类型查询
     */
    List<AuditLog> findByActionType(String actionType);

    /**
     * 根据用户 ID 查询
     */
    List<AuditLog> findByUserId(Long userId);
}