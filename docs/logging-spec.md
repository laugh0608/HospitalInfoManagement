# 日志规范文档

> 文档版本：v1.1
> 最后更新：2026-02-25

---

## 1. 日志系统概述

本项目采用统一的日志系统，实现以下功能：
- 请求日志（HTTP 请求追踪）
- 业务日志（业务操作记录）
- 审计日志（关键操作行为）
- SQL 日志（数据库操作）
- 错误日志（异常信息）

---

## 2. 日志文件说明

| 文件 | 说明 | 保留时间 |
|------|------|----------|
| `hospital.log` | 应用主日志 | 30 天 |
| `hospital-error.log` | 错误日志 | 90 天 |
| `access.log` | HTTP 访问日志 | 30 天 |
| `audit.log` | 审计日志 | 90 天 |
| `sql.log` | SQL 执行日志 | 7 天 |

---

## 3. 日志格式

### 3.1 应用日志格式

```
2026-02-25 14:30:15.123 INFO  [a1b2c3d4] 1234 --- [http-nio-8080-exec-1] c.g.h.service.PatientService : 创建病人成功: patientNo=P2026022500001, name=张三
```

**字段说明**：
- 时间戳
- 日志级别（DEBUG/INFO/WARN/ERROR）
- 请求 ID（用于链路追踪）
- 进程 ID
- 线程名
- Logger 名称
- 日志内容

### 3.2 审计日志格式

```
2026-02-25 14:30:15 | AUDIT | 用户: admin | 操作: 登录 | 模块: 认证 | 描述: 用户登录成功
```

**字段说明**：
- 操作时间
- 日志类型
- 操作用户
- 操作类型
- 操作模块
- 操作描述

---

## 4. 请求日志

### 4.1 功能说明

请求日志拦截器会自动记录每个 HTTP 请求：
- 请求 URL、Method
- 请求参数
- 响应状态码
- 请求耗时
- 客户端 IP
- User-Agent

### 4.2 日志示例

```
→ POST /api/v1/patients | IP: 192.168.1.100 | UA: Mozilla/5.0...
← POST /api/v1/patients | Status: 201 | Duration: 156ms
```

### 4.3 请求 ID

每个请求会生成唯一请求 ID：
- 响应头：`X-Request-Id`
- MDC 中的 key：`requestId`
- 可用于日志关联查询

---

## 5. 审计日志

### 5.1 审计操作类型

| 操作类型 | 说明 |
|----------|------|
| LOGIN | 用户登录 |
| LOGOUT | 用户登出 |
| LOGIN_FAILED | 登录失败 |
| CREATE | 数据创建 |
| UPDATE | 数据更新 |
| DELETE | 数据删除 |
| QUERY | 数据查询 |
| EXPORT | 数据导出 |
| IMPORT | 数据导入 |
| PASSWORD_CHANGE | 修改密码 |
| PERMISSION_CHANGE | 权限变更 |

### 5.2 审计日志 API

```java
@Autowired
private AuditLogger auditLogger;

// 记录登录成功
auditLogger.logLoginSuccess(username, userId, ip);

// 记录登录失败
auditLogger.logLoginFailed(username, ip, reason);

// 记录创建操作
auditLogger.logCreate("病人管理", "创建病人档案", patientId);

// 记录更新操作
auditLogger.logUpdate("病人管理", "更新病人信息", patientId);

// 记录删除操作
auditLogger.logDelete("病人管理", "删除病人档案", patientId);
```

---

## 6. SQL 日志

### 6.1 配置说明

SQL 日志单独输出到 `sql.log` 文件：
- 记录所有 SQL 语句
- 记录参数绑定
- 保留 7 天

### 6.2 日志级别

| 级别 | 说明 |
|------|------|
| DEBUG | SQL 语句 |
| TRACE | 参数绑定详情 |

---

## 7. 业务日志规范

### 7.1 日志级别使用

| 级别 | 使用场景 |
|------|----------|
| DEBUG | 调试信息（开发环境） |
| INFO | 正常业务流程（创建、更新、删除等） |
| WARN | 警告信息（业务异常） |
| ERROR | 错误信息（系统异常） |

### 7.2 日志格式规范

**创建操作**：
```java
log.info("创建病人成功: patientNo={}, name={}", patientNo, name);
```

**更新操作**：
```java
log.info("更新病人信息成功: id={}", id);
```

**删除操作**：
```java
log.info("删除病人成功: id={}", id);
```

**异常记录**：
```java
log.error("获取病人失败: id={}", id, e);
```
> 注意：异常日志必须记录完整的上下文参数，不要向客户端暴露堆栈信息。

---

## 8. 日志配置

### 8.1 配置文件位置

`src/main/resources/logback-spring.xml`

### 8.2 环境配置

- **开发环境** (`dev`)：控制台 + 文件输出
- **生产环境** (`prod`)：仅文件输出

### 8.3 滚动策略

- 按大小滚动：单个文件最大 10MB
- 按时间滚动：按天滚动
- 保留时间：应用日志 30 天，错误日志 90 天

---

## 9. 日志使用示例

### 9.1 在 Service 中记录日志

```java
@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    @Override
    @Transactional
    public Patient createPatient(Patient patient) {
        // 业务逻辑...

        log.info("创建病人成功: patientNo={}, name={}",
                saved.getPatientNo(), saved.getName());

        return saved;
    }
}
```

### 9.2 在 Controller 中记录审计日志

```java
@RestController
@RequiredArgsConstructor
public class PatientController {

    private final AuditLogger auditLogger;

    @PostMapping
    public Result<Patient> createPatient(@RequestBody Patient patient) {
        Patient saved = patientService.createPatient(patient);

        // 记录审计日志
        auditLogger.logCreate("病人管理", "创建病人档案", saved.getId());

        return Result.success(saved);
    }
}
```

---

## 10. 日志查询

### 10.1 查询请求日志

```bash
# 查看特定请求 ID 的日志
grep "a1b2c3d4" logs/hospital.log

# 查看特定 URL 的请求
grep "/api/v1/patients" logs/hospital.log
```

### 10.2 查询审计日志

```bash
# 查看今天的审计日志
cat logs/audit.log | grep "2026-02-25"

# 查看用户操作记录
grep "admin" logs/audit.log
```

### 10.3 查询 SQL 日志

```bash
# 查看特定表的 SQL
grep "patient" logs/sql.log
```

---

## 11. 数据库日志（可选）

### 11.1 概述

除了文件日志，系统还支持将日志写入独立的 SQLite 数据库，具有以下优势：
- 更快的查询速度
- 支持复杂条件查询
- 节省磁盘空间
- 易于数据分析和导出

### 11.2 目录结构

```
db/
├── hospital.db          # 业务数据库
└── hospital.log.db     # 日志数据库

logs/                   # 保留文件日志（可选关闭）
└── ...
```

### 11.3 配置选项

在 `application.properties` 中配置：

```properties
# 文件日志开关（默认开启）
logging.file.enabled=true

# 数据库日志配置
logging.db.enabled=true                                    # 启用数据库日志
logging.db.split-mode=DAY                                   # 分表模式：YEAR/MONTH/WEEK/DAY
logging.db.retention-days=90                                # 数据保留天数
logging.db.log-sql=true                                    # 是否记录 SQL 日志
logging.db.log-audit=true                                  # 是否记录审计日志
logging.db.log-access=true                                  # 是否记录访问日志
logging.db.database-path=db/hospital.log.db                # 日志数据库路径
logging.db.batch-size=100                                   # 批量插入大小
logging.db.flush-interval=5000                              # 刷新间隔（毫秒）
```

### 11.4 分表模式

| 模式 | 说明 | 表名示例 | 适用场景 |
|------|------|----------|----------|
| YEAR | 按年分表 | sql_2026 | 低流量日志 |
| MONTH | 按月分表 | sql_202602 | 中等流量 |
| WEEK | 按周分表 | sql_2026W01 | 高流量 |
| DAY | 按天分表 | sql_20260225 | 高流量/精细分析 |

### 11.5 数据库表结构

**SQL 日志表（sql_YYYYMMDD）**
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键 |
| log_time | DATETIME | 日志时间 |
| thread | TEXT | 线程名 |
| sql_type | TEXT | SQL 类型 |
| sql_text | TEXT | SQL 语句 |
| duration | INTEGER | 执行耗时 |
| success | INTEGER | 是否成功 |
| error_message | TEXT | 错误信息 |
| username | TEXT | 用户名 |
| request_id | TEXT | 请求 ID |

**审计日志表（audit_YYYYMMDD）**
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键 |
| log_time | DATETIME | 日志时间 |
| username | TEXT | 用户名 |
| user_id | INTEGER | 用户 ID |
| action_type | TEXT | 操作类型 |
| module | TEXT | 模块 |
| description | TEXT | 描述 |
| target | TEXT | 目标对象 |
| ip | TEXT | IP 地址 |
| success | INTEGER | 是否成功 |

**访问日志表（access_YYYYMMDD）**
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键 |
| request_time | DATETIME | 请求时间 |
| request_id | TEXT | 请求 ID |
| method | TEXT | 请求方法 |
| url | TEXT | 请求 URL |
| ip | TEXT | 客户端 IP |
| user_agent | TEXT | 用户代理 |
| status | INTEGER | 响应状态码 |
| duration | INTEGER | 耗时（毫秒） |
| username | TEXT | 用户名 |

### 11.6 启用/禁用日志类型

根据需求可以单独开启或关闭某类日志：

```properties
# 只开启审计日志（节省空间）
logging.db.log-sql=false
logging.db.log-audit=true
logging.db.log-access=false
```

### 11.7 数据库日志查询

```bash
# 使用 sqlite3 查询日志数据库
sqlite3 db/hospital.log.db

# 查询今天的审计日志
SELECT * FROM audit_20260225 WHERE username = 'admin';

# 查询今天的访问日志
SELECT * FROM access_20260225 WHERE status = 404;

# 查询慢 SQL（耗时 > 1秒）
SELECT * FROM sql_20260225 WHERE duration > 1000;
```

### 11.8 实现状态

| 功能 | 状态 | 说明 |
|------|------|------|
| 访问日志写入 SQLite | ✅ 已实现 | 通过 DbLoggingFilter 拦截 HTTP 请求 |
| 审计日志写入 SQLite | ✅ 已实现 | 通过 AuditLogger 记录并写入数据库 |
| SQL 日志写入 SQLite | ✅ 已实现 | 通过 SqlLoggingInterceptor 拦截 Hibernate SQL |
| 分表存储 | ✅ 已实现 | 支持 YEAR/MONTH/WEEK/DAY 模式 |
| 批量写入 | ✅ 已实现 | batch-size=100，flush-interval=5000ms |
| 数据清理 | ✅ 已实现 | 根据 retention-days 配置自动清理过期分表 |

### 11.9 注意事项

- 日志清理任务每天自动执行，根据 `retention-days` 配置删除过期分表
- 清理任务首次延迟 1 分钟执行，避免影响应用启动
- 分表名格式需符合规范，否则无法解析日期进行清理
```