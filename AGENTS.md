# AGENTS.md - 编码规范指南

本文档为在 HospitalInfoManagement 项目中工作的 AI 代理提供编码指南。

## 构建命令

### Backend (Gradle/Java)

```bash
cd backend

# 运行应用
./gradlew bootRun

# 构建
./gradlew build              # 构建 JAR
./gradlew clean              # 清理构建产物

# 运行测试
./gradlew test                                               # 所有测试
./gradlew test --tests "com.hospital.ClassName"              # 单个测试类
./gradlew test --tests "*.methodName"                        # 单个测试方法
./gradlew test --info                                        # 详细输出

# 生成 API 文档
./gradlew asciidoctor                                        # AsciiDoc
./gradlew asciidoctor pdf                                     # PDF
```

### Frontend (Vite/Vue)

```bash
cd frontend

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

## 代码风格指南

### Java (后端)

**命名约定：**
- 类名：`PascalCase`（例如：`PatientService`、`HospitalController`）
- 方法/变量：`camelCase`（例如：`getPatientById`、`patientList`）
- 常量：`SCREAMING_SNAKE_CASE`（例如：`MAX_RETRY_COUNT`）
- 包名：全小写（例如：`com.hospital.service`）

**文件结构：**
```
src/main/java/com/graduation/hospital/
├── entity/           # JPA 实体类
├── repository/       # Spring Data JPA 仓储
├── service/          # 业务逻辑
├── controller/       # REST API
├── dto/              # 数据传输对象
├── vo/               # 视图对象
├── common/           # 工具类、配置
└── HospitalApplication.java
```

**导入规范：**
- 避免使用通配符导入，使用显式导入
- 顺序：static → java.* → javax.* → org.* → com.hospital.*

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

---

### Vue/TypeScript (前端)

**命名约定：**
- 组件：`PascalCase`（例如：`PatientTable.vue`）
- 文件：工具类使用 `kebab-case`（例如：`api-client.ts`）
- Props/Emits：`camelCase`

**文件结构：**
```
src/
├── api/              # API 服务
├── components/       # 可复用组件
├── views/            # 页面组件
├── stores/           # Pinia 状态管理
├── router/           # Vue Router 配置
├── utils/            # 工具函数
├── types/            # TypeScript 接口
└── main.ts
```

**TypeScript：**
- 启用严格模式（noImplicitAny, strictNullChecks）
- 为 API 响应定义接口
- 使用proper类型，避免使用 `any`
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

**数据库（MySQL + Spring Data JPA）：**
- 表名：`snake_case`（例如：`patient_info`）
- 实体字段：`camelCase`（JPA 自动映射）
- 非持久化字段使用 `@Transient`
- 主键：`Long id` 配合 `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`
- 关联关系使用 `@OneToMany`、`@ManyToOne`、`@ManyToMany` 等注解
- 使用 `@Column` 注解显式指定列名映射（可选但推荐）

**API 响应格式：**
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

**Git 提交规范：**
- 格式：`type(scope): description`
- 类型：feat, fix, docs, style, refactor, test, chore, perf, ci
- 示例：`feat(patient): 添加患者搜索功能`
- **本仓库使用仓库级 Git 配置，提交时使用仓库作者信息**
  - user: laugh0608
  - email: laugh0608@foxmail.com
- 禁止在提交信息中包含任何 AI 协作者相关信息

---

## 数据库配置

- 创建 MySQL 数据库：`hospital`
- 将 `application-example.properties` 复制为 `application.properties`
- 在 `application.properties` 中配置 MySQL 连接信息
- 运行 `db/` 目录下的数据库初始化脚本