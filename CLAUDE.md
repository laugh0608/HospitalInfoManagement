# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

HospitalInfoManagement 是一个社区医院病人信息管理系统，基于以下技术栈构建：
- **后端**：Spring Boot 4.0.1、MySQL、MyBatis Plus、Java 25
- **前端**：Vue 3、TypeScript、Vite、Element Plus（规划中）

## 构建命令

### 后端（Gradle）
```bash
cd backend

./gradlew bootRun              # 运行应用
./gradlew build                 # 构建 JAR 包
./gradlew clean                 # 清理构建产物
./gradlew test                  # 运行所有测试
./gradlew test --tests "ClassName"        # 运行指定测试类
./gradlew test --tests "*.methodName"      # 运行指定测试方法
./gradlew asciidoctor           # 生成 API 文档（AsciiDoc 格式）
```

### 前端（npm/Vite）
```bash
cd frontend

npm install                     # 安装依赖
npm run dev                     # 启动开发服务器（热重载）
npm run build                   # 生产环境构建
npm run preview                 # 预览生产环境构建
npx vue-tsc                     # TypeScript 类型检查
```

## 架构设计

### 后端分层结构
```
src/main/java/com/graduation/hospital/
├── entity/           # JPA 实体类（数据库表映射）
├── mapper/           # MyBatis 映射器
├── service/          # 业务逻辑层
├── controller/       # REST API 控制器
├── dto/              # 数据传输对象
├── vo/               # 视图对象（响应包装）
├── common/           # 公共工具类
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
├── router/           # Vue Router 配置
├── utils/            # 工具函数
├── types/            # TypeScript 类型定义
└── main.ts
```

## 核心规范

### API 响应格式
所有接口统一返回：`{ code: 200, message: "success", data: {...} }`

### 数据库规范
- 表名使用 `snake_case`（例如：`patient_info`）
- 实体字段使用 `camelCase`（MyBatis Plus 自动映射）
- 非数据库字段使用 `@TableField(exist = false)`
- 主键：`Long id` 配合 `@TableId(type = IdType.AUTO)`

### Git 提交规范

**提交格式：**
```
<type>(<scope>): <subject>
```

**type 类型说明：**

| type | 说明 | 示例 |
|------|------|------|
| `feat` | 新功能 | `feat(patient): 添加患者搜索功能` |
| `fix` | Bug 修复 | `fix(controller): 修复患者查询空指针异常` |
| `docs` | 文档更新 | `docs: 更新 API 文档` |
| `style` | 代码格式（不影响功能） | `style: 格式化代码缩进` |
| `refactor` | 重构（不修改功能） | `refactor(service): 简化患者服务逻辑` |
| `test` | 测试相关 | `test: 添加患者服务单元测试` |
| `chore` | 构建配置、工具更新 | `chore: 更新依赖版本` |
| `perf` | 性能优化 | `perf: 优化查询性能` |
| `ci` | CI 配置 | `ci: 添加自动化测试流程` |

**注意事项：**
- 本仓库使用仓库级 Git 配置，提交时使用仓库作者信息（user: laugh0608, email: laugh0608@foxmail.com）
- 提交信息禁止包含任何 AI 协作者相关信息
- 使用中文描述，简洁明了
- scope 为可选项，表示影响范围（如模块名）
- subject 不超过 50 字

## 开发注意事项

### Java
- 使用 Lombok `@Slf4j` 进行日志记录
- 写操作必须添加 `@Transactional` 注解
- Mapper 方法参数需添加 `@Param` 注解
- 使用 `List<>` 而非原始 `ArrayList`
- 时间类型使用 `LocalDateTime`

### Vue 3 + TypeScript
- 使用 `<script setup lang="ts">` 语法
- Props 使用 `readonly` 确保类型安全
- 使用 `defineEmits<>()` 实现类型化事件
- 导入别名：`@/` 指向 `src/` 目录
- 严格 TypeScript 模式：禁止隐式 `any`、禁止使用 `any`

## 配置说明

- 后端配置模板：`backend/src/main/resources/application-example.properties`
- 需复制为 `application.properties` 并配置 MySQL 连接信息
- 需提前创建 MySQL 数据库：`hospital`

## 参考资料

- 完整编码规范：[AGENTS.md](AGENTS.md)
- 项目介绍：[README.md](README.md)
- Spring Boot 参考文档：[backend/HELP.md](backend/HELP.md)