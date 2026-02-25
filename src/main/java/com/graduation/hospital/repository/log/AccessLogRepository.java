package com.graduation.hospital.repository.log;

import com.graduation.hospital.entity.log.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 访问日志数据访问仓储
 * 注意：实际表名为分表名称（access_YYYYMMDD）
 */
@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    /**
     * 根据时间范围查询
     */
    List<AccessLog> findByRequestTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据请求 ID 查询
     */
    List<AccessLog> findByRequestId(String requestId);

    /**
     * 根据用户查询
     */
    List<AccessLog> findByUsername(String username);

    /**
     * 根据状态码查询
     */
    List<AccessLog> findByStatus(Integer status);
}