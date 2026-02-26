# 前端架构设计

> 文档版本：v26.2.4
> 最后更新：2026-02-26

---

## 技术栈

- **框架**：Vue 3
- **语言**：TypeScript
- **构建工具**：Vite
- **UI 组件库**：Element Plus
- **状态管理**：Pinia
- **路由**：Vue Router
- **HTTP 客户端**：Axios

---

## 项目结构

```
frontend/src/
├── api/                    # API 服务层（axios 封装）
│   ├── index.ts           # axios 实例 + 请求/响应拦截器
│   └── modules/           # 按模块划分的 API
│       └── auth.ts        # 认证相关 API
├── assets/               # 静态资源
│   └── style.css         # 全局样式
├── components/           # 可复用组件
├── layouts/              # 布局组件
│   └── MainLayout.vue    # 侧边栏 + 顶栏 + 内容区
├── views/                # 页面组件
│   ├── home/             # 门户首页（公共）
│   ├── login/            # 登录页（公共）
│   ├── dashboard/        # 仪表盘
│   ├── patient/          # 患者管理
│   ├── doctor/           # 医生管理
│   ├── department/       # 科室管理
│   ├── appointment/      # 预约管理
│   ├── medical-record/   # 病历管理
│   └── medicine/         # 药品管理
├── stores/               # Pinia 状态管理
│   └── user.ts           # 用户认证 store
├── router/               # Vue Router 配置
│   └── index.ts          # 路由 + 导航守卫
├── types/                # TypeScript 类型定义
│   ├── api.ts            # API 响应类型
│   ├── auth.ts           # 认证相关类型
│   ├── entity.ts         # 实体类型
│   └── router.d.ts       # 路由 meta 类型扩展
├── utils/                # 工具函数
│   └── token.ts          # Token 管理
├── App.vue
└── main.ts
```

---

## 路由结构

```
/                           # 门户首页（公共，无需认证）
/login                      # 登录页（公共，无需认证）
/console                    # 管理后台（需认证，MainLayout）
├── /console/dashboard      # 仪表盘（默认首页）
├── /console/patient        # 患者管理
├── /console/doctor         # 医生管理
├── /console/department     # 科室管理
├── /console/appointment    # 预约挂号
├── /console/medical-record # 病历管理
└── /console/medicine       # 药品管理
```

> **说明**：
> - `/` 和 `/login` 为公共路由，无需登录即可访问
> - 已登录用户访问 `/login` 会重定向到 `/console`
> - 未登录用户访问 `/console/*` 路由会重定向到 `/login`
> - 门户首页根据用户角色动态显示「管理后台」入口

---

## 状态管理

### 用户认证 Store

```typescript
// stores/user.ts
- state: token, userInfo, roles
- actions: login(), logout(), fetchUserInfo()
- getters: isAuthenticated, userRoles
```

### Token 管理

```typescript
// utils/token.ts
- getToken(): string | null
- setToken(token: string): void
- removeToken(): void
```

---

## API 封装

### 请求拦截器

- 自动添加 Authorization header
- 处理请求错误
- 显示加载状态

### 响应拦截器

- 统一处理错误码
- 处理 Token 过期（自动刷新或跳转登录）
- 解析响应数据

### API 模块划分

```
api/modules/
├── auth.ts       # 登录、登出、获取用户信息
├── patient.ts    # 患者管理
├── doctor.ts     # 医生管理
├── department.ts # 科室管理
├── appointment.ts# 预约管理
├── medicalRecord.ts # 病历管理
└── medicine.ts   # 药品管理
```

---

## 布局组件

### MainLayout

```
┌─────────────────────────────────────────────────────────┐
│  顶栏（Logo、用户信息、设置、登出）                      │
├────────────┬────────────────────────────────────────────┤
│            │                                            │
│  侧边栏    │           内容区域                         │
│  （菜单）  │           （RouterView）                   │
│            │                                            │
│            │                                            │
└────────────┴────────────────────────────────────────────┘
```

---

## 组件规范

### 命名约定

- 组件文件：`PascalCase`（例如：`PatientTable.vue`）
- 组件目录：`kebab-case`（例如：`patient-table/`）

### Props/Emits

- 使用 TypeScript 接口定义 Props
- 使用 `defineEmits<>()` 实现类型安全的事件
- 使用 `readonly` 标记只读属性

### 模板规范

- 模板属性超过 3 个时换行
- 使用 2 空格缩进

---

## 相关文件

| 文件 | 说明 |
|------|------|
| [project-plan.md](../project-plan.md) | 项目整体规划 |
| [后端架构](backend.md) | 后端架构设计 |
| [API 接口](../api/endpoints.md) | API 接口清单 |