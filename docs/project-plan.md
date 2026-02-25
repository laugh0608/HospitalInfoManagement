# HospitalInfoManagement 项目规划

> 文档状态：已完善
> 最后更新：2026-02-25

## 项目概述

### 项目背景

社区医院作为基层医疗服务的重要载体，承担着为社区居民提供基本医疗保健服务的重要职责。随着社区人口的增长和医疗服务需求的日益多样化，传统的人工管理模式已经难以满足现代社区医院的管理需求。本项目旨在通过信息化手段，建立一套完善的社区医院病人信息管理系统，实现病人信息管理、病历跟踪、预约挂号、药品管理等核心业务的数字化、智能化。

**目标用户**：
- 社区医院管理人员：需要全面掌握医院运营数据，进行决策支持
- 医护人员（医生、护士）：需要高效管理病人信息、诊疗记录
- 患者：需要便捷的预约挂号、查询个人诊疗信息

**解决的问题**：
- 病人信息分散、查询不便的问题
- 病历记录不规范、难以追溯的问题
- 预约挂号流程繁琐、效率低下的问题
- 药品库存管理混乱、库存预警不及时的问题

### 项目目标

本项目的核心目标是构建一个功能完善、安全可靠、易于使用的社区医院病人信息管理系统，具体包括：

1. **信息化管理**：实现病人信息、医疗记录、药品库存的电子化管理
2. **流程优化**：优化预约挂号、就诊、处方等业务流程，提高效率
3. **数据支撑**：提供统计分析功能，为医院管理决策提供数据支持
4. **服务提升**：为患者提供便捷的线上服务，提升就医体验

## 功能规划

### 核心功能模块

#### 1. 用户管理模块
- [ ] 用户注册与登录
- [ ] 角色权限管理
- [ ] 个人信息管理

#### 2. 病人信息管理模块
- [ ] 病人档案管理
- [ ] 病人信息查询
- [ ] 病人信息统计

#### 3. 病历管理模块
- [ ] 病历记录
- [ ] 病历查询
- [ ] 病历历史追踪

#### 4. 预约管理模块
- [ ] 预约挂号
- [ ] 预约查询
- [ ] 预约提醒

#### 5. 医护人员管理模块
- [ ] 医护人员信息管理
- [ ] 排班管理
- [ ] 工作记录

#### 6. 药品管理模块
- [ ] 药品库存管理
- [ ] 药品出入库
- [ ] 药品信息查询

#### 7. 统计报表模块
- [ ] 数据统计分析
- [ ] 报表生成
- [ ] 数据导出

### 功能优先级

根据业务重要性和开发依赖关系，功能模块的开发优先级如下：

| 优先级 | 模块 | 说明 |
|--------|------|------|
| P0 | 用户与权限 | 系统基础，所有功能的前提 |
| P0 | 病人信息管理 | 核心业务，其他模块依赖 |
| P1 | 医护人员管理 | 医生、科室等基础数据管理 |
| P1 | 预约管理 | 病人就诊的入口业务流程 |
| P1 | 病历管理 | 核心诊疗数据的记录与管理 |
| P2 | 药品管理 | 辅助诊疗，库存管理 |
| P3 | 统计报表 | 管理决策支持，可后期完善 |

## 技术架构

### 技术栈
- **后端**：Spring Boot 4.0.1 + Spring Data JPA + Gradle
- **前端**：Vue 3 + TypeScript + Vite + Element Plus
- **数据库**：SQLite (开发) / MySQL (生产)
- **安全**：Spring Security
- **其他**：Redis、WebSocket

### 架构设计

#### 系统架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        前端 (Vue 3)                         │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐       │
│  │ 路由    │  │ 状态管理│  │ API调用 │  │ UI组件  │       │
│  └────┬────┘  └────┬────┘  └────┬────┘  └────┬────┘       │
└───────┼────────────┼────────────┼────────────┼─────────────┘
        │            │            │            │
        └────────────┴────────────┴────────────┘
                          │ HTTP/REST
                          ▼
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

#### 后端分层结构

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

#### 前端项目结构

```
frontend/src/
├── api/           # API 服务层（axios 封装）
├── components/    # 可复用组件
│   ├── common/   # 通用组件（表格、表单、弹窗等）
│   └── business/ # 业务组件
├── views/        # 页面组件
│   ├── patients/    # 病人管理页面
│   ├── doctors/     # 医生管理页面
│   ├── appointments/ # 预约管理页面
│   ├── medical-records/ # 病历管理页面
│   ├── medicines/   # 药品管理页面
│   ├── statistics/ # 统计报表页面
│   └── layout/      # 布局组件
├── stores/       # Pinia 状态管理
│   ├── auth.ts      # 认证状态
│   ├── user.ts      # 用户信息
│   └── patients.ts # 病人数据
├── router/      # Vue Router 配置
├── types/       # TypeScript 类型定义
└── utils/       # 工具函数
```

#### API 设计规范

- 列表查询：`GET /api/{resource}`，支持分页、排序、过滤
- 详情查询：`GET /api/{resource}/{id}`
- 创建资源：`POST /api/{resource}`
- 更新资源：`PUT /api/{resource}/{id}`
- 删除资源：`DELETE /api/{resource}/{id}`
- 关联查询：`GET /api/{resource}/{id}/{relation}`

#### 前端路由结构

```
/                           # 根路由（重定向到登录或仪表盘）
├── /login                  # 登录页（无需认证）
├── /register               # 注册页（无需认证）
└── /                       # 主布局（需认证）
    ├── /dashboard          # 首页/仪表盘
    ├── /patients           # 病人管理
    │   ├── /patients/list         # 病人列表
    │   ├── /patients/add          # 新增病人
    │   └── /patients/:id          # 病人详情
    ├── /doctors            # 医生管理
    │   ├── /doctors/list          # 医生列表
    │   ├── /doctors/add           # 新增医生
    │   └── /doctors/:id           # 医生详情
    ├── /departments       # 科室管理
    ├── /appointments      # 预约管理
    │   ├── /appointments/list    # 预约列表
    │   ├── /appointments/add     # 新增预约
    │   └── /appointments/calendar # 预约日历
    ├── /medical-records   # 病历管理
    │   ├── /medical-records/list  # 病历列表
    │   ├── /medical-records/add   # 新增病历
    │   └── /medical-records/:id   # 病历详情
    ├── /medicines         # 药品管理
    │   ├── /medicines/list       # 药品列表
    │   ├── /medicines/stock      # 库存管理
    │   └── /medicines/warning    # 库存预警
    ├── /statistics        # 统计报表
    │   ├── /statistics/overview  # 数据概览
    │   ├── /statistics/patients # 病人统计
    │   └── /statistics/revenue  # 营收统计
    └── /settings          # 系统设置
        ├── /settings/profile    # 个人资料
        └── /settings/users      # 用户管理（仅管理员）
```

**统一响应格式（Result.java）**：
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1234567890
}
```

---

## 数据库架构设计

### 核心实体（8张表）

| 表名 | 说明 | 关联 |
|------|------|------|
| `sys_user` | 系统用户 | OneToOne -> user_profile, ManyToMany -> sys_role |
| `sys_role` | 角色 | ManyToMany -> sys_permission |
| `patient` | 病人信息 | OneToMany -> medical_record, appointment |
| `doctor` | 医生信息 | OneToOne -> user, ManyToOne -> department |
| `department` | 科室 | OneToMany -> doctor, appointment |
| `medical_record` | 病历记录 | ManyToOne -> patient, doctor |
| `appointment` | 预约挂号 | ManyToOne -> patient, doctor, department |
| `medicine` | 药品信息 | OneToMany -> medicine_stock |
| `medicine_stock` | 药品库存 | ManyToOne -> medicine |

### 关键字段设计

#### patient（病人信息表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 主键，自增 |
| patientNo | String | 病人编号（唯一） |
| name | String | 姓名 |
| gender | String | 性别 |
| birthDate | LocalDate | 出生日期 |
| idCard | String | 身份证号 |
| phone | String | 联系电话 |
| insuranceType | String | 医保类型 |
| allergies | String | 过敏史 |
| medicalHistory | String | 既往病史 |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

#### doctor（医生信息表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 主键，自增 |
| doctorNo | String | 医生编号（唯一） |
| name | String | 姓名 |
| title | String | 职称（主任医师/副主任医师/主治医师/住院医师） |
| specialty | String | 专业领域 |
| yearsOfExperience | Integer | 工作年限 |
| departmentId | Long | 所属科室 |
| isAvailable | Boolean | 是否可用 |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

#### medical_record（病历记录表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 主键，自增 |
| recordNo | String | 病历编号（唯一） |
| patientId | Long | 病人ID |
| doctorId | Long | 医生ID |
| visitTime | LocalDateTime | 就诊时间 |
| visitType | String | 就诊类型（门诊/急诊/住院） |
| chiefComplaint | String | 主诉 |
| diagnosis | String | 诊断 |
| treatmentPlan | String | 治疗方案 |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

#### appointment（预约挂号表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 主键，自增 |
| appointmentNo | String | 预约编号（唯一） |
| patientId | Long | 病人ID |
| doctorId | Long | 医生ID |
| departmentId | Long | 科室ID |
| appointmentTime | LocalDateTime | 预约时间 |
| status | String | 状态（PENDING/CONFIRMED/COMPLETED/CANCELLED） |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

#### medicine（药品信息表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 主键，自增 |
| medicineCode | String | 药品编码（唯一） |
| name | String | 药品名称 |
| specification | String | 规格 |
| price | BigDecimal | 价格 |
| category | String | 分类（西药/中成药/中药） |
| stock | Integer | 当前库存 |
| minStock | Integer | 最低库存预警值 |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

#### department（科室表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 主键，自增 |
| name | String | 科室名称 |
| code | String | 科室编码 |
| description | String | 科室描述 |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

#### sys_user（系统用户表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 主键，自增 |
| username | String | 用户名（唯一） |
| password | String | 密码（加密存储） |
| email | String | 邮箱 |
| enabled | Boolean | 是否启用 |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

#### sys_role（角色表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 主键，自增 |
| name | String | 角色名称 |
| code | String | 角色编码 |
| description | String | 角色描述 |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

---

## 安全与权限设计

### 用户角色

| 角色 | 编码 | 权限 |
|------|------|------|
| 管理员 | ADMIN | 全部权限 |
| 医生 | DOCTOR | 病人、病历、预约管理 |
| 护士 | NURSE | 病人、预约、药品管理 |
| 患者 | PATIENT | 预约挂号、查看自己病历 |

### 认证机制

- **认证方式**：JWT 无状态认证
- **Token 存储**：Authorization header（Bearer Token）
- **Token 包含信息**：username、roles、expiration
- **Token 有效期**：24小时（可配置）
- **刷新机制**：Token 过期前自动刷新

### 权限控制

- 使用 Spring Security 方法级注解：`@PreAuthorize("hasRole('ADMIN')")`
- 前端路由守卫：未登录用户无法访问需要认证的页面
- API 级别权限控制：后端校验用户角色和权限

---

## 业务流程

### 病人就诊流程

```
病人入院 -> 挂号/预约 -> 医生接诊 -> 病历记录 -> 处方开具 -> 药房发药
```

#### 详细流程说明

1. **病人入院**：患者首次就诊需注册个人基本信息
2. **挂号/预约**：患者选择科室、医生、就诊时间进行预约
3. **医生接诊**：医生根据预约时间接诊患者
4. **病历记录**：医生填写病历，包括主诉、诊断、治疗方案
5. **处方开具**：医生根据诊断结果开具处方
6. **药房发药**：药房根据处方配药、发药

---

## 开发计划

### 阶段划分（15周）

| 阶段 | 周数 | 目标 | 主要内容 |
|------|------|------|----------|
| 阶段一 | 2周 | 基础架构 | 包结构、通用类（Result、异常处理）、前端框架集成 |
| 阶段二 | 2周 | 用户与权限 | 登录注册、JWT认证、角色权限 |
| 阶段三 | 3周 | 病人与医护管理 | 病人CRUD、医生管理、科室管理 |
| 阶段四 | 2周 | 预约管理 | 挂号、排班、预约确认 |
| 阶段五 | 2周 | 病历管理 | 病历创建、查询、历史追踪 |
| 阶段六 | 2周 | 药品管理 | 药品CRUD、出入库、库存预警 |
| 阶段七 | 2周 | 统计报表 | 指标统计、图表展示、数据导出 |
| 阶段八 | 2周 | 测试与优化 | 单元测试、集成测试、Bug修复 |

### 里程碑

| 里程碑 | 时间点 | 目标 |
|--------|--------|------|
| M1 | 第2周 | 基础框架搭建完成（后端分层、前端基础） |
| M2 | 第4周 | 用户认证完成（登录、注册、JWT、权限） |
| M3 | 第9周 | 核心业务上线（病人、医生、病历、预约） |
| M4 | 第11周 | 药品库存上线（药品管理、出入库） |
| M5 | 第13周 | 统计报表上线（数据统计、图表导出） |
| M6 | 第15周 | 系统上线（测试通过、稳定运行） |

## 团队协作

### 角色分工

| 角色 | 职责 |
|------|------|
| 项目经理 | 项目整体规划、进度把控、资源协调 |
| 后端开发 | 业务逻辑实现、API开发、数据库设计 |
| 前端开发 | 页面开发、组件封装、交互实现 |
| 测试工程师 | 测试用例编写、功能测试、缺陷跟踪 |
| UI/UX 设计 | 界面设计、用户体验优化 |

### 开发规范

详见项目根目录的 `CLAUDE.md` 和 `AGENTS.md` 文档。

### 代码管理

- 使用 Git 进行版本控制
- 代码托管：GitHub
- 分支策略：feature/*（功能开发）、bugfix/*（Bug修复）
- 代码审查：Pull Request 必须经过至少一人审查后方可合并

### 版本号规范

- **格式**：`vYY.M.N`
  - `YY`：年份后两位（如 26 表示 2026 年）
  - `M`：月份（如 2 表示 2 月份）
  - `N`：当月发布的序号（从 1 开始）
- **示例**：
  - `v26.2.1` = 2026 年 2 月第 1 个版本
  - `v26.5.3` = 2026 年 5 月第 3 个版本
- **版本标签**：使用 Git Tag 标记发布版本，格式：`vYY.M.N`

---

## 风险评估

### 技术风险

| 风险 | 影响程度 | 应对措施 |
|------|----------|----------|
| JWT 认证安全 | 高 | 使用强密钥、定期轮换、限制 Token 有效期 |
| 数据库性能 | 中 | 合理索引优化、SQL 语句优化 |
| 前端安全性 | 中 | XSS 防护、CSRF 令牌验证 |
| Spring Boot 版本兼容性 | 低 | 关注官方版本更新、及时升级 |

### 资源风险

| 风险 | 影响程度 | 应对措施 |
|------|----------|----------|
| 开发时间不足 | 中 | 优先级排序、分阶段交付 |
| 人员变动 | 高 | 文档规范、知识共享 |
| 技术难点 | 中 | 预留技术调研时间、寻求外部帮助 |

### 应对策略

1. **技术风险应对**：
   - 采用业界成熟的安全实践
   - 建立完善的测试机制
   - 定期进行代码审查

2. **资源风险应对**：
   - 制定详细的项目计划
   - 保持敏捷开发节奏
   - 建立知识库

## 附录

### 参考资料

- Spring Boot 官方文档：https://spring.io/projects/spring-boot
- Vue 3 官方文档：https://vuejs.org/
- Element Plus 官方文档：https://element-plus.org/
- Spring Security 参考：https://spring.io/projects/spring-security
- JWT 规范：https://jwt.io/
- MySQL 官方文档：https://www.mysql.com/

### 相关文件

| 文件 | 说明 |
|------|------|
| `CLAUDE.md` | 快速入门指南 |
| `AGENTS.md` | AI 编码规范 |
| `README.md` | 项目介绍 |
| `backend/HELP.md` | Spring Boot 参考文档 |
| `database-design.md` | 数据库设计文档 |
| `api-design.md` | API 接口设计文档 |

### 变更记录
| 日期 | 版本 | 变更内容 | 变更人 |
|------|------|----------|--------|
| 2026-01-20 | v0.1 | 创建文档框架 | - |
| 2026-02-25 | v1.0 | 完善架构规划文档，补充数据库设计、后端架构、前端架构、安全设计、业务流程、开发计划等内容 | - |
| 2026-02-25 | v1.1 | 新增数据库设计文档（database-design.md）和 API 设计文档（api-design.md） | - |
| 2026-02-25 | v26.2.1 | 完善版本号规范；完成后端核心架构（Entity、Repository、Service、Controller） | - |
