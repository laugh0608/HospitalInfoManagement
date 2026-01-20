# HospitalInfoManagement

社区医院病人跟踪信息管理系统

## 项目简介

本项目是一个基于 Java (SpringBoot) 和 Vue 3 (Vite) 的社区医院病人信息管理系统，旨在帮助社区医院高效管理病人信息、跟踪病人健康状况、提供便捷的医疗服务。

## 技术栈

### 后端
- **Spring Boot 4.0.1** - 应用框架
- **MySQL 8.0+** - 数据库
- **Spring Data JPA** - ORM 框架
- **Gradle (Groovy DSL)** - 构建工具
- **Java 25** - 编程语言
- **Spring Security** - 安全框架
- **Spring Boot Actuator** - 监控管理
- **Redis** - 缓存（可选）
- **WebSocket** - 实时通信

### 前端
- **Vue 3** - 前端框架
- **Vite** - 构建工具
- **Pinia** - 状态管理
- **Vue Router** - 路由管理
- **Element Plus** - UI 组件库

## 主要功能

- [ ] 病人信息管理
- [ ] 病历跟踪
- [ ] 预约管理
- [ ] 医护人员管理
- [ ] 药品管理
- [ ] 统计报表

## 项目结构

```
HospitalInfoManagement/
├── backend/          # Spring Boot 后端项目
├── frontend/         # Vue 3 前端项目
├── docs/             # 项目文档
└── README.md         # 项目说明
```

## 快速开始

### 环境要求
- JDK 25+
- Node.js 18+
- SQLite（开发环境，自动创建）/ MySQL 8.0+（生产环境）

### 后端启动

```bash
cd backend

# 1. 首次使用需要给 gradlew 添加执行权限（Linux/Mac）
chmod +x gradlew

# 2. Gradle 会自动下载依赖（首次运行较慢）
# Windows 使用：gradlew.bat bootRun
# Linux/Mac 使用：./gradlew bootRun

# 3. 启动应用（开发环境使用 SQLite，无需额外配置数据库）
./gradlew bootRun

# 或者先构建再运行
./gradlew build
java -jar build/libs/hospital-0.0.1-SNAPSHOT.jar
```

**说明：**
- 项目使用 Gradle Wrapper，无需手动安装 Gradle
- 开发环境默认使用 SQLite，数据库文件自动创建在 `backend/db/hospital.db`
- 如需使用 MySQL，请参考 `backend/src/main/resources/application-example.properties` 配置
- Java 25 的本地访问警告已通过 `gradle.properties` 配置解决

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

## 许可证

本项目采用 MIT 许可证，详见 [LICENSE](LICENSE) 文件。