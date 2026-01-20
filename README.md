# HospitalInfoManagement

社区医院病人跟踪信息管理系统

## 项目简介

本项目是一个基于 Java (SpringBoot) 和 Vue 3 (Vite) 的社区医院病人信息管理系统，旨在帮助社区医院高效管理病人信息、跟踪病人健康状况、提供便捷的医疗服务。

## 技术栈

### 后端
- **Spring Boot** - 应用框架
- **MySQL** - 数据库
- **MyBatis Plus** - ORM 框架
- **其他常用依赖**

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
- JDK 17+
- Node.js 18+
- MySQL 8.0+

### 后端启动

```bash
cd backend
# 配置数据库连接
# 导入数据库脚本
./mvnw spring-boot:run
```

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

## 许可证

本项目采用 MIT 许可证，详见 [LICENSE](LICENSE) 文件。