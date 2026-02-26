# 开发指南

> 文档版本：v1.0
> 最后更新：2026-02-26

本目录包含项目开发所需的各类指南文档。

---

## 目录

### 开发规范

| 文档 | 说明 |
|------|------|
| [development-spec.md](../development-spec.md) | 代码编写规范、命名约定 |

### 安全配置

| 文档 | 说明 |
|------|------|
| [security-config.md](../security-config.md) | 安全配置说明（JWT、权限） |

### 日志规范

| 文档 | 说明 |
|------|------|
| [logging-spec.md](../logging-spec.md) | 日志规范（文件日志 + 数据库日志） |

### 运行配置

| 文档 | 说明 |
|------|------|
| [run-config.md](run-config.md) | 运行配置说明 |

---

## 快速开始

### 后端开发

1. 配置数据库连接（参考 [run-config.md](run-config.md)）
2. 运行 `./gradlew bootRun` 启动后端服务

### 前端开发

1. 进入前端目录：`cd frontend`
2. 安装依赖：`npm install`
3. 运行开发服务器：`npm run dev`

---

## 相关文件

| 文件 | 说明 |
|------|------|
| [项目规划](../project-plan.md) | 项目整体规划 |
| [后端架构](../architecture/backend.md) | 后端架构设计 |
| [前端架构](../architecture/frontend.md) | 前端架构设计 |