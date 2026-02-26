# 项目文档

HospitalInfoManagement 项目文档索引。

## 目录结构

```
docs/
├── README.md                    # 文档索引（本文）
├── CHANGELOG.md                 # 版本变更日志
├── project-plan.md              # 项目规划
├── architecture/                # 架构文档
│   ├── backend.md              # 后端架构
│   └── frontend.md             # 前端架构
├── database/                    # 数据库文档
│   ├── design.md               # 数据库设计（ER图、原则）
│   └── schema.md               # 表结构详情
├── api/                         # API 文档
│   ├── overview.md             # API 设计规范
│   └── endpoints.md            # 接口清单
├── guide/                       # 开发指南
│   ├── development.md          # 开发指南入口
│   ├── security.md             # 安全配置
│   ├── logging.md              # 日志规范
│   └── run-config.md           # 运行配置
├── development-spec.md          # 开发规范（引用）
├── security-config.md           # 安全配置（引用）
├── logging-spec.md              # 日志规范（引用）
└── development-logs/            # 开发日志
    └── ...
```

## 文档说明

### 项目规划
| 文档 | 说明 |
|------|------|
| [project-plan.md](project-plan.md) | 项目整体规划、功能模块、技术选型、开发计划 |
| [CHANGELOG.md](CHANGELOG.md) | 版本发布记录 |

### 架构文档
| 文档 | 说明 |
|------|------|
| [architecture/backend.md](architecture/backend.md) | 后端架构设计、技术栈、分层结构 |
| [architecture/frontend.md](architecture/frontend.md) | 前端架构设计、技术栈、项目结构 |

### 数据库文档
| 文档 | 说明 |
|------|------|
| [database/design.md](database/design.md) | 数据库设计原则、实体关系图 |
| [database/schema.md](database/schema.md) | 数据库表结构详情、SQL 定义 |

### API 文档
| 文档 | 说明 |
|------|------|
| [api/overview.md](api/overview.md) | API 设计规范、RESTful 约定 |
| [api/endpoints.md](api/endpoints.md) | 完整 API 接口清单 |

### 开发指南
| 文档 | 说明 |
|------|------|
| [guide/development.md](guide/development.md) | 开发指南入口 |
| [guide/run-config.md](guide/run-config.md) | 运行配置说明 |
| [development-spec.md](development-spec.md) | 代码编写规范、命名约定 |
| [security-config.md](security-config.md) | 安全配置说明（JWT、权限） |
| [logging-spec.md](logging-spec.md) | 日志规范（文件日志 + 数据库日志） |

### 开发日志
| 目录 | 说明 |
|------|------|
| [development-logs/](development-logs/) | 按周记录的开发过程 |

## 相关链接

- [项目根目录 README](../README.md) - 快速开始指南
- [CLAUDE.md](../CLAUDE.md) - Claude Code 开发指南
- [AGENTS.md](../AGENTS.md) - AI 编码规范