# 版本变更日志

本文档记录 HospitalInfoManagement 项目的所有版本发布。

## 版本格式

- **格式**：`vYY.M.N`
  - `YY`：年份后两位（如 26 表示 2026 年）
  - `M`：月份（如 2 表示 2 月份）
  - `N`：当月发布的序号（从 1 开始）

## 变更日志

### v26.2.3 (2026-02-26)

**新增**
- 搭建前端基础设施，实现登录页和主布局
- 集成 Element Plus、Vue Router、Pinia、Axios

**修复**
- 后端登录白名单路径配置

---

### v26.2.2 (2026-02-25)

**新增**
- 数据库日志服务，支持分表存储
- DbLoggingService：数据库日志服务类
- DbLoggingFilter：请求日志拦截器
- SqlLoggingInterceptor：SQL 执行日志拦截器

---

### v26.2.1 (2026-02-25)

**新增**
- 后端核心架构
- Entity：实体类（User、Role、Patient、Doctor、Department、MedicalRecord、Appointment、Medicine）
- Repository：JPA 仓储接口
- Service：业务逻辑层（接口+实现）
- Controller：REST API 控制器
- 安全认证：JWT 无状态认证
- 统一响应格式：Result.java

---

### v0.1 (2026-01-20)

**新增**
- 创建文档框架
- 项目初始化