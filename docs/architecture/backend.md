# 后端架构设计

> 文档版本：v26.2.3
> 最后更新：2026-02-26

---

## 技术栈

- **框架**：Spring Boot 4.0.1
- **数据访问**：Spring Data JPA + Hibernate
- **构建工具**：Gradle (Groovy DSL)
- **数据库**：SQLite（开发）/ MySQL（生产）
- **安全**：Spring Security + JWT
- **日志**：Logback（文件）+ 自研日志库（SQLite 分表存储）

---

## 系统架构图

```
┌─────────────────────────────────────────────────────────────┐
│                     后端 (Spring Boot)                       │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐       │
│  │Controller│  │ Service │  │Repository│  │  Entity │       │
│  └────┬────┘  └────┬────┘  └────┬────┘  └────┬────┘       │
└───────┼────────────┼────────────┼────────────┼─────────────┘
        │            │            │            │
        └────────────┴────────────┴────────────┘
                          │ JPA
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                      数据库 (MySQL)                         │
└─────────────────────────────────────────────────────────────┘
```

---

## 分层结构

```
src/main/java/com/graduation/hospital/
├── config/           # 配置类（SecurityConfig、CorsConfig、JpaConfig）
├── entity/           # 实体类（8个核心实体）
├── repository/       # JPA 仓储接口
├── service/         # 业务逻辑层（接口+实现）
├── controller/      # REST API 控制器
├── dto/             # 数据传输对象（Request）
├── vo/              # 视图对象（Response、Result、PageResult）
├── common/          # 公共工具
│   ├── exception/  # 自定义异常
│   ├── handler/    # 全局异常处理器
│   └── util/       # 工具类
└── security/       # 安全相关（JWT）
```

---

## 核心模块

### Entity（实体层）

| 实体 | 说明 | 关联 |
|------|------|------|
| `SysUser` | 系统用户 | OneToOne -> SysUserProfile, ManyToMany -> SysRole |
| `SysRole` | 角色 | ManyToMany -> SysPermission |
| `Patient` | 病人信息 | OneToMany -> MedicalRecord, Appointment |
| `Doctor` | 医生信息 | OneToOne -> SysUser, ManyToOne -> Department |
| `Department` | 科室 | OneToMany -> Doctor, Appointment |
| `MedicalRecord` | 病历记录 | ManyToOne -> Patient, Doctor |
| `Appointment` | 预约挂号 | ManyToOne -> Patient, Doctor, Department |
| `Medicine` | 药品信息 | OneToMany -> MedicineStock |

### Repository（仓储层）

- 所有 Repository 接口继承 `JpaRepository<Entity, ID>` 或 `CrudRepository<Entity, ID>`
- 支持自定义查询方法（方法名解析）
- 支持 `@Query` 注解自定义 JPQL

### Service（业务层）

- 接口 + 实现类分离
- 事务管理（`@Transactional`）
- 日志记录（`@Slf4j`）

### Controller（控制层）

- RESTful 风格
- 统一返回 `Result<T>` 对象
- 参数校验（`@Valid`）

---

## API 设计规范

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1234567890
}
```

### RESTful 规范

| 方法 | 说明 | 幂等性 |
|------|------|--------|
| GET | 查询资源 | 幂等 |
| POST | 创建资源 | 非幂等 |
| PUT | 完整更新资源 | 幂等 |
| PATCH | 部分更新资源 | 非幂等 |
| DELETE | 删除资源 | 幂等 |

### URL 命名规范

- 资源名称使用复数形式：`/api/patients`
- 层级关系用路径表示：`/api/patients/{id}/medical-records`
- 动名词结合：`/api/appointments/{id}/confirm`

### 分页、排序、过滤

```
GET /api/patients?page=0&size=20&sort=name,asc&gender=男
```

| 参数 | 说明 | 默认值 |
|------|------|--------|
| page | 页码（从 0 开始） | 0 |
| size | 每页数量 | 20 |
| sort | 排序字段及方向 | createdAt,desc |

---

## 安全设计

### 认证机制

- **方式**：JWT 无状态认证
- **Token 存储**：Authorization header（Bearer Token）
- **Token 包含信息**：username、roles、expiration
- **Token 有效期**：24小时（可配置）

### 权限控制

- 使用 Spring Security 方法级注解：`@PreAuthorize("hasRole('ADMIN')")`
- API 级别权限控制：后端校验用户角色和权限

### 用户角色

| 角色 | 编码 | 权限 |
|------|------|------|
| 管理员 | ADMIN | 全部权限 |
| 医生 | DOCTOR | 病人、病历、预约管理 |
| 护士 | NURSE | 病人、预约、药品管理 |
| 患者 | PATIENT | 预约挂号、查看自己病历 |

---

## 日志设计

### 日志类型

| 类型 | 存储方式 | 说明 |
|------|----------|------|
| 应用日志 | 文件 | Logback 写入文件 |
| SQL 日志 | SQLite | 记录执行的 SQL 语句 |
| 审计日志 | SQLite | 记录用户操作行为 |

### 相关文档

- [日志规范](../guide/logging.md)
- [安全配置](../guide/security.md)

---

## 相关文件

| 文件 | 说明 |
|------|------|
| [project-plan.md](../project-plan.md) | 项目整体规划 |
| [前端架构](frontend.md) | 前端架构设计 |
| [数据库设计](../database/design.md) | 数据库表结构 |
| [API 设计](../api/overview.md) | API 设计规范 |