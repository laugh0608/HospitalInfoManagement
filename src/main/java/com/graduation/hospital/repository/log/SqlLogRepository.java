package com.graduation.hospital.repository.log;

import com.graduation.hospital.entity.log.SqlLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SQL 日志数据访问仓储
 * 注意：实际表名为分表名称（sql_YYYYMMDD）
 */
@Repository
public interface SqlLogRepository extends JpaRepository<SqlLog, Long> {

    /**
     * 根据时间范围查询
     */
    List<SqlLog> findByLogTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据请求 ID 查询
     */
    List<SqlLog> findByRequestId(String requestId);

    /**
     * 根据用户名查询
     */
    List<SqlLog> findByUsername(String username);
}