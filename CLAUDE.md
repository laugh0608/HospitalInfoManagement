# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

HospitalInfoManagement 是一个社区医院病人信息管理系统，基于以下技术栈构建：
- **后端**：Spring Boot 4.0.1、MySQL、JPA + Spring Data、Java 25、Gradle (Groovy DSL)
- **前端**：Vue 3、TypeScript、Vite、Element Plus（规划中）

## 代码编写流程

**在编写任何代码之前，必须遵循以下流程：**

1. **描述方案**：先向用户清晰描述你的实现方案，包括：
   - 目标功能或修复的问题
   - 涉及的代码文件和模块
   - 实现的思路和关键步骤
   - 预期的代码变更

2. **等待批准**：等待用户确认方案后再开始编写代码

3. **澄清问题**：如果需求不明确，务必在编写代码之前提出并澄清问题

---

## 构建命令

### 后端（Gradle/Java）

```bash
# 运行应用
./gradlew bootRun

# 构建
./gradlew build              # 构建 JAR
./gradlew clean              # 清理构建产物

# 运行测试
./gradlew test                                               # 所有测试
./gradlew test --tests "com.graduation.hospital.ClassName"  # 单个测试类
./gradlew test --tests "*.methodName"                        # 单个测试方法
./gradlew test --info                                        # 详细输出

# 生成 API 文档
./gradlew asciidoctor                                        # AsciiDoc
./gradlew asciidoctor pdf                                     # PDF
```

### 前端（Vite/Vue）

```bash
cd frontend

# 安装依赖
npm install                           # 安装依赖

# 开发
npm run dev                           # 热重载开发

# 构建
npm run build                         # 生产环境构建

# 预览
npm run preview                       # 预览生产环境构建

# 类型检查
npx vue-tsc                           # 严格类型检查
```

---

## 架构设计

### 后端分层结构

```
src/main/java/com/graduation/hospital/
├── entity/           # JPA 实体类
├── repository/       # Spring Data JPA 仓储
├── service/          # 业务逻辑
├── controller/       # REST API
├── dto/              # 数据传输对象
├── vo/               # 视图对象
├── common/           # 工具类、配置
│   ├── exception/    # 自定义异常
│   └── Result.java   # 统一 API 响应封装
└── HospitalApplication.java
```

### 前端结构（Vue 3）

```
src/
├── api/              # API 服务层
├── components/       # 可复用组件
├── views/            # 页面组件
├── stores/           # Pinia 状态管理
├── router/          # Vue Router 配置
├── utils/           # 工具函数
├── types/           # TypeScript 类型定义
└── main.ts
```

---

## 代码风格指南

### Java (后端)

**命名约定：**
- 类名：`PascalCase`（例如：`PatientService`、`HospitalController`）
- 方法/变量：`camelCase`（例如：`getPatientById`、`patientList`）
- 常量：`SCREAMING_SNAKE_CASE`（例如：`MAX_RETRY_COUNT`）
- 包名：全小写（例如：`com.graduation.hospital.service`）

**导入规范：**
- 避免使用通配符导入，使用显式导入
- 顺序：static → java.* → javax.* → org.* → com.graduation.hospital.*

**类型使用：**
- 不需要空安全时使用基本类型（`int` 而非 `Integer`）
- 使用 `List<>` 而非原始 `ArrayList`
- 时间戳使用 `LocalDateTime`，而非 `Date`

**错误处理：**
- 在 `common/exception/` 中使用自定义异常
- 返回一致的 API 响应（参见 `common/Result.java`）
- 使用上下文记录错误：`log.error("获取患者失败: id={}", id, e)`
- 永远不要向客户端暴露堆栈跟踪

**其他规范：**
- 写操作必须添加 `@Transactional`
- Repository 接口继承 `JpaRepository<Entity, ID>` 或 `CrudRepository<Entity, ID>`
- 使用 Lombok `@Slf4j` 记录日志
- 方法尽量控制在 50 行以内
- 实体类必须使用 JPA 注解（`@Entity`、`@Table`、`@Column`）
- 主键使用 `@Id` 和 `@GeneratedValue(strategy = GenerationType.IDENTITY)`

**配置文件注释规范：**
- `.properties` 和 `.yml` 配置文件必须使用英文注释
- 禁止在配置文件中使用中文注释（包括行内注释），避免编码问题导致配置解析错误
- 示例：`jwt.expiration=86400000  # valid for 24 hours`（正确）
- 示例：`jwt.expiration=86400000  # 24小时`（错误 - 会导致解析失败）

---

### Vue/TypeScript (前端)

**命名约定：**
- 组件：`PascalCase`（例如：`PatientTable.vue`）
- 文件：工具类使用 `kebab-case`（例如：`api-client.ts`）
- Props/Emits：`camelCase`

**TypeScript：**
- 启用严格模式（noImplicitAny, strictNullChecks）
- 为 API 响应定义接口
- 使用 proper 类型，避免使用 `any`
- Props 使用 `readonly`

**Vue 3 Composition API：**
- 使用 `<script setup lang="ts">` 语法
- 使用 TypeScript 接口定义 Props
- 使用 `defineEmits<>()` 实现类型安全的事件
- 将 ref 保持在 `<script setup>` 顶层

**导入规范：**
- 使用 `@/` 别名导入 src 目录下的模块
- 分组导入：外部依赖 → 内部模块 → 组件
- 工具函数使用命名导出

**格式化：**
- 使用 2 空格缩进
- 模板属性超过 3 个时换行
- TypeScript 语句使用分号

---

## 项目约定

### 数据库（MySQL + Spring Data JPA）

- 表名：`snake_case`（例如：`patient_info`）
- 实体字段：`camelCase`（JPA 自动映射）
- 非持久化字段使用 `@Transient`
- 主键：`Long id` 配合 `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`
- 关联关系使用 `@OneToMany`、`@ManyToOne`、`@ManyToMany` 等注解
- 使用 `@Column` 注解显式指定列名映射（可选但推荐）

### API 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### Git 提交规范

- 格式：`type(scope): description`
- 类型：feat, fix, docs, style, refactor, test, chore, perf, ci
- 示例：`feat(patient): 添加患者搜索功能`
- **本仓库使用仓库级 Git 配置，提交时使用仓库作者信息**
  - user: laugh0608
  - email: laugh0608@foxmail.com
- 禁止在提交信息中包含任何 AI 协作者相关信息
- 使用中文描述，简洁明了
- subject 不超过 50 字

### 版本号规范

- **格式**：`vYY.M.N`
  - `YY`：年份后两位（如 26 表示 2026 年）
  - `M`：月份（如 2 表示 2 月份）
  - `N`：当月发布的序号（从 1 开始）
- **示例**：
  - `v26.2.1` = 2026 年 2 月第 1 个版本
  - `v26.5.3` = 2026 年 5 月第 3 个版本
- **版本标签**：使用 Git Tag 标记发布版本，格式：`vYY.M.N`
- **版本记录**：每次发布需在 CHANGELOG.md 中记录变更内容

### 代码注释规范

- **原则**：在关键位置添加中文注释，保持代码易读性和可维护性
- **需要加注释的场景**：
  - 业务逻辑复杂的地方，说明业务规则
  - 难以理解的算法或计算逻辑
  - 特殊处理或边界条件
  - 重要的配置或常量，说明其含义
  - 公共方法或接口，说明其用途和参数
- **不需要加注释的场景**：
  - 简单的 getter/setter
  - 显而易见的代码逻辑
  - 纯粹的模板代码
- **示例**：

```java
// 病人编号格式：P + 年月日 + 5位序号
private String generatePatientNo() {
    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    long sequence = patientNoCounter.incrementAndGet();
    return "P" + date + String.format("%05d", sequence % 100000);
}
```

---

## 数据库配置

- 创建 MySQL 数据库：`hospital`
- 将 `application-example.properties` 复制为 `application.properties`
- 在 `application.properties` 中配置 MySQL 连接信息
- 运行 `db/` 目录下的数据库初始化脚本

---

## 参考资料

- 完整编码规范：[AGENTS.md](AGENTS.md)
- 项目介绍：[README.md](README.md)
- Spring Boot 参考文档：[HELP.md](HELP.md)