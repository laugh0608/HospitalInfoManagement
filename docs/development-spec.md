# HospitalInfoManagement 开发规范

> 文档版本：v26.2.3
> 最后更新：2026-02-26
> 维护团队：Hospital 开发组

---

## 1. 概述

### 1.1 目的和范围

本文档旨在为 HospitalInfoManagement 项目建立统一的开发规范，涵盖后端、前端、数据库、Git 工作流等方面的技术标准和最佳实践。本规范适用于所有项目成员，旨在确保代码质量、提高开发效率、降低维护成本。

### 1.2 技术栈

| 层级 | 技术选型 | 版本 |
|------|----------|------|
| 后端框架 | Spring Boot | 4.0.1 |
| 开发语言 | Java | 25 |
| 构建工具 | Gradle (Groovy DSL) | 9.x |
| ORM 框架 | Spring Data JPA | - |
| 数据库 | MySQL (生产) / SQLite (开发) | 8.0+ |
| 安全框架 | Spring Security + JWT | - |
| 前端框架 | Vue 3 | 3.x |
| 前端语言 | TypeScript | 5.x |
| 构建工具 | Vite | 6.x |
| UI 组件库 | Element Plus | 2.x |
| 状态管理 | Pinia | 2.x |

---

## 2. 后端规范

### 2.1 分层架构

项目采用标准四层架构，各层职责清晰，依赖方向从外到内：

```
src/main/java/com/graduation/hospital/
├── entity/              # 实体层 - 数据库表映射
├── repository/          # 仓储层 - 数据访问接口
├── service/             # 业务层 - 业务逻辑处理
├── controller/          # 控制层 - REST API 接口
├── dto/                 # 数据传输对象 - 请求参数
├── vo/                  # 视图对象 - 响应数据
├── common/              # 公共组件
│   ├── exception/       # 自定义异常
│   ├── handler/         # 全局处理器
│   └── util/            # 工具类
└── config/              # 配置类
```

**各层职责：**

| 层级 | 职责 | 规则 |
|------|------|------|
| Entity | 数据库表映射，JPA 注解 | 不包含业务逻辑 |
| Repository | 数据持久化操作 | 继承 JpaRepository，只做数据存取 |
| Service | 业务逻辑处理 | 事务管理，异常抛出 |
| Controller | HTTP 请求处理 | 参数校验，权限控制，响应封装 |
| DTO/VO | 数据传输转换 | 根据业务需求定义 |

**层间调用规则：**
- Controller → Service → Repository → Entity
- 禁止跨层调用（如 Controller 直接访问 Repository）
- 同层之间可以相互调用

### 2.2 命名规范

#### 2.2.1 类命名

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 实体类 | PascalCase，与表名对应 | `Patient`, `MedicalRecord` |
| Repository | Entity名称 + Repository | `PatientRepository` |
| Service 接口 | I + Entity名称 + Service | `IPatientService` |
| Service 实现 | Entity名称 + ServiceImpl | `PatientServiceImpl` |
| Controller | Entity名称 + Controller | `PatientController` |
| DTO | 功能 + Request/Response | `PatientCreateRequest` |
| VO | 功能 + VO | `PatientDetailVO` |
| 异常类 | 名称 + Exception | `BusinessException` |
| 配置类 | 功能 + Config | `SecurityConfig` |

#### 2.2.2 方法命名

| 场景 | 命名规则 | 示例 |
|------|----------|------|
| 查询单个 | `get` + 实体名 + By + 条件 | `getPatientById` |
| 查询列表 | `get` + 实体名 + 列表 | `getAllPatients` |
| 条件查询 | `find` + 实体名 + By + 条件 | `findPatientsByName` |
| 保存 | `create` / `save` + 实体名 | `createPatient` |
| 更新 | `update` + 实体名 | `updatePatient` |
| 删除 | `delete` / `remove` + 实体名 | `deletePatient` |
| 判断存在 | `exists` + 实体名 + By + 条件 | `existsPatientByIdCard` |
| 统计 | `count` + 实体名 | `countPatients` |

#### 2.2.3 变量命名

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 普通变量 | camelCase | `patientList`, `userName` |
| 常量 | SCREAMING_SNAKE_CASE | `MAX_RETRY_COUNT` |
| 成员变量 | camelCase，忌用缩写 | `departmentId`（不用 deptId） |
| 局部变量 | 简洁明了，避免单字符 | `existingPatient`（不用 p） |

#### 2.2.4 包命名

- 全部小写
- 用途明确：如 `com.graduation.hospital.entity`
- 禁止使用通配符导入

### 2.3 代码规范

#### 2.3.1 注解使用

```java
// 实体类
@Entity
@Table(name = "patient")
@Data                       // Lombok
@EqualsAndHashCode(callSuper = true)

// Repository
@Repository                // 可省略，Spring Data JPA 自动注册

// Service
@Service
@RequiredArgsConstructor  // Lombok，生成构造函数注入
@Transactional             // 写操作必须添加
@Slf4j                    // Lombok，日志

// Controller
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
```

#### 2.3.2 事务规范

- 所有写操作（INSERT、UPDATE、DELETE）必须添加 `@Transactional`
- 读取操作一般不需要事务（需要可加 `@Transactional(readOnly = true)`）
- 事务默认回滚 RuntimeException，可通过 `rollbackFor` 指定其他异常
- 避免长事务，方法体控制在合理范围

#### 2.3.3 日志规范

```java
// 记录关键业务操作
log.info("创建病人成功: patientNo={}, name={}", saved.getPatientNo(), saved.getName());

// 记录业务异常
log.warn("业务异常: {}", e.getMessage());

// 记录系统异常
log.error("系统异常: ", e);

// 禁止记录敏感信息（密码、身份证号等）
```

#### 2.3.4 方法长度控制

- 单个方法控制在 50 行以内
- 超过 30 行应考虑拆分
- 避免深层嵌套（不超过 3 层）

#### 2.3.5 代码注释规范

- **原则**：在关键位置添加中文注释，保持代码易读性和可维护性，**不要所有的地方都加注释**
- **需要加注释的场景**：
  - 业务逻辑复杂的地方，说明业务规则
  - 难以理解的算法或计算逻辑
  - 特殊处理或边界条件
  - 重要的配置或常量，说明其含义
  - 公共方法或接口，说明其用途和参数
  - 编号生成规则、状态流转等业务逻辑
- **不需要加注释的场景**：
  - 简单的 getter/setter
  - 显而易见的代码逻辑
  - 纯粹的模板代码
  - 单行显而易见的功能

#### 配置文件注释规范

- `.properties` 和 `.yml` 配置文件必须使用 **英文注释**
- **禁止**在配置文件中使用中文注释（包括行内注释），避免编码问题导致配置解析错误
- 示例（正确）：`jwt.expiration=86400000  # valid for 24 hours`
- 示例（错误）：`jwt.expiration=86400000  # 24小时`

```java
// 病人编号格式：P + 年月日 + 5位序号
private String generatePatientNo() {
    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    long sequence = patientNoCounter.incrementAndGet();
    return "P" + date + String.format("%05d", sequence % 100000);
}
```

### 2.4 API 设计规范

#### 2.4.1 RESTful 风格

| 操作 | HTTP 方法 | URL 模式 | 示例 |
|------|-----------|----------|------|
| 查询全部 | GET | /api/{resource} | GET /api/patients |
| 查询单个 | GET | /api/{resource}/{id} | GET /api/patients/1 |
| 条件查询 | GET | /api/{resource}/search | GET /api/patients/search?keyword=张三 |
| 创建 | POST | /api/{resource} | POST /api/patients |
| 更新 | PUT | /api/{resource}/{id} | PUT /api/patients/1 |
| 删除 | DELETE | /api/{resource}/{id} | DELETE /api/patients/1 |

#### 2.4.2 URL 命名规范

- 资源名称使用复数形式
- 使用小写字母，用连字符分隔（kebab-case）
- 避免动词，使用 HTTP 方法表达动作
- 关联资源：`/api/patients/1/medical-records`

```yaml
# 正确示例
GET    /api/patients
GET    /api/patients/1
POST   /api/appointments
GET    /api/doctors/available

# 错误示例
GET    /api/getPatients      # 包含动词
GET    /api/patient           # 单数形式
POST   /api/patient/create    # URL 包含动作
```

#### 2.4.3 HTTP 方法语义

| 方法 | 语义 | 幂等 |
|------|------|------|
| GET | 查询资源 | 是 |
| POST | 创建资源 | 否 |
| PUT | 完整更新资源 | 是 |
| PATCH | 部分更新资源 | 否 |
| DELETE | 删除资源 | 是 |

#### 2.4.4 API 版本控制

项目采用 **URL 路径版本控制** 方式，在 URL 中直接指定 API 版本。

| 版本 | 路径 | 说明 |
|------|------|------|
| v1 | `/api/v1/...` | 默认版本，生产环境使用 |
| v2 | `/api/v2/...` | 新功能版本，测试中或暂未上线 |

```yaml
# v1 版本（默认）
GET    /api/v1/patients
POST   /api/v1/patients
GET    /api/v1/patients/1

# v2 版本（新功能）
GET    /api/v2/patients
POST   /api/v2/patients/export   # v2 新增的导出功能
```

**版本管理原则：**
- 所有新功能默认添加到 v2
- v1 保持稳定，不轻易变更
- 新功能在 v2 测试稳定后，可升级到 v1
- 旧版本 API 至少保留 3 个月再考虑废弃

**Controller 配置：**

```java
@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {
    // v1 版本 API
}

@RestController
@RequestMapping("/api/v2/patients")
public class PatientV2Controller {
    // v2 版本 API，包含新功能
}
```

### 2.5 请求响应规范

#### 2.5.1 统一响应格式

```json
// 成功响应
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "张三"
  },
  "timestamp": 1737792000
}

// 分页响应
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [],
    "totalElements": 100,
    "totalPages": 10,
    "pageNumber": 0,
    "pageSize": 10
  },
  "timestamp": 1737792000
}

// 错误响应
{
  "code": 400,
  "message": "参数错误：病人不存在",
  "data": null,
  "timestamp": 1737792000
}
```

#### 2.5.2 响应码规范

| 状态码 | 含义 | 使用场景 |
|--------|------|----------|
| 200 | 成功 | 正常业务处理成功 |
| 201 | 创建成功 | POST 创建资源成功 |
| 400 | 参数错误 | 请求参数校验失败 |
| 401 | 未认证 | 未登录或 Token 过期 |
| 403 | 无权限 | 没有访问权限 |
| 404 | 资源不存在 | 查询的资源不存在 |
| 409 | 业务冲突 | 业务规则冲突（如重复数据） |
| 500 | 系统错误 | 服务器内部异常 |

#### 2.5.3 请求参数规范

```java
// 路径参数
@GetMapping("/{id}")
public Result<Patient> getPatientById(@PathVariable Long id)

// 查询参数
@GetMapping
public Result<PageResult<Patient>> getAllPatients(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(required = false) String keyword)

// 请求体
@PostMapping
public Result<Patient> createPatient(@RequestBody @Valid Patient patient)

// 文件上传
@PostMapping("/import")
public Result<Void> importPatients(@RequestParam("file") MultipartFile file)
```

### 2.6 异常处理规范

#### 2.6.1 异常分类

| 异常类型 | 处理方式 | 示例 |
|----------|----------|------|
| 业务异常 (IllegalArgumentException) | 返回 400，提示具体错误 | 参数校验、重复数据 |
| 权限异常 (AccessDeniedException) | 返回 403 | 无权限访问 |
| 资源不存在 | 返回 404 | 查询不存在的资源 |
| 系统异常 (Exception) | 返回 500，记录日志 | 未知异常 |

#### 2.6.2 全局异常处理

使用 `@RestControllerAdvice` 统一处理所有异常：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(400, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error(500, "系统内部错误");
    }
}
```

#### 2.6.3 异常抛出规范

- Service 层抛出业务异常
- Controller 层不捕获异常，交给全局处理器
- 异常信息对内详细，对外友好
- 禁止抛出 NullPointerException，使用 Optional 或断言

### 2.7 数据库规范

#### 2.7.1 表命名规范

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 表名 | snake_case，复数 | `sys_user`, `medical_records` |
| 主键列 | `id` | - |
| 外键列 | `{表名}_id` | `patient_id`, `doctor_id` |
| 时间戳列 | `created_at`, `updated_at` | - |
| 布尔列 | `is_` 前缀 | `is_available`, `is_enabled` |
| 枚举列 | `status`, `type`, `category` | - |

#### 2.7.2 字段类型选择

| 场景 | 类型 | 示例 |
|------|------|------|
| 主键 | BIGINT + 自增 | `id BIGINT AUTO_INCREMENT` |
| 字符串 | VARCHAR | 固定长度用 CHAR |
| 文本 | TEXT | 超过 255 字符 |
| 日期 | DATETIME / DATE | 含时间用 DATETIME |
| 金额 | DECIMAL(10,2) | 精确计算 |
| 布尔 | TINYINT(1) | 0/1 |

#### 2.7.3 索引规范

- 主键自动建索引
- 外键列添加索引
- 频繁查询条件添加索引
- 避免过多索引影响写入性能

---

## 3. 前端规范

### 3.1 项目结构

```
frontend/src/
├── api/                    # API 服务层
│   ├── index.ts           # axios 实例 + 请求/响应拦截器
│   └── modules/           # 按模块划分的 API
│       └── auth.ts        # 认证 API（login/register/getCurrentUser）
├── assets/               # 静态资源
│   └── style.css         # 全局样式
├── components/            # 通用组件
├── layouts/              # 布局组件
│   └── MainLayout.vue    # 侧边栏 + 顶栏 + 内容区
├── views/                 # 页面组件
│   ├── login/            # 登录页
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
│   └── index.ts          # 路由注册 + 导航守卫
├── types/                # TypeScript 类型定义
│   ├── api.ts            # API 响应类型（Result<T>、PageResult<T>）
│   ├── auth.ts           # 认证类型（LoginRequest、AuthResponse）
│   ├── entity.ts         # 实体类型
│   └── router.d.ts       # 路由 meta 类型扩展
├── utils/                # 工具函数
│   └── token.ts          # Token 管理（localStorage）
├── App.vue
└── main.ts
```

### 3.2 命名规范

#### 3.2.1 文件命名

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| Vue 组件 | PascalCase | `PatientTable.vue`, `DoctorForm.vue` |
| 类型文件 | kebab-case | `api-client.ts`, `patient-types.ts` |
| 工具文件 | kebab-case | `date-utils.ts`, `http-utils.ts` |
| 配置文件 | kebab-case | `vite.config.ts`, `axios.config.ts` |
| 目录 | kebab-case | `patient-list/`, `medical-records/` |

#### 3.2.2 组件命名

- 文件名：PascalCase
- 组件内部名称：PascalCase
- Props：camelCase
- 事件：kebab-case

```vue
<!-- PatientTable.vue -->
<script setup lang="ts">
defineProps<{
  patients: Patient[];
  loading: boolean;
}>();

const emit = defineEmits<{
  (e: 'edit', id: number): void;
  (e: 'delete', id: number): void;
}>();
</script>
```

### 3.3 API 调用规范

#### 3.3.1 服务层封装

```typescript
// api/patient.ts
import request from './index';
import type { Patient, PageResult } from '@/types';

export const getPatients = (params: PatientQueryParams) =>
  request.get<{ data: PageResult<Patient> }>('/api/patients', { params });

export const getPatientById = (id: number) =>
  request.get<{ data: Patient }>(`/api/patients/${id}`);

export const createPatient = (data: Partial<Patient>) =>
  request.post<{ data: Patient }>('/api/patients', data);

export const updatePatient = (id: number, data: Partial<Patient>) =>
  request.put<{ data: Patient }>(`/api/patients/${id}`, data);

export const deletePatient = (id: number) =>
  request.delete<void>(`/api/patients/${id}`);
```

#### 3.3.2 错误处理

```typescript
import { ElMessage } from 'element-plus';

request.interceptors.response.use(
  (response) => response,
  (error) => {
    const { response } = error;
    if (response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录');
      // 跳转登录
    } else if (response?.status === 403) {
      ElMessage.error('没有权限执行此操作');
    } else {
      ElMessage.error(response?.data?.message || '请求失败');
    }
    return Promise.reject(error);
  }
);
```

### 3.4 状态管理规范

#### 3.4.1 Store 划分

| Store | 职责 | 持久化 |
|-------|------|--------|
| user | 登录状态、Token、用户信息 | Token → localStorage |

#### 3.4.2 Store 定义规范

```typescript
// stores/user.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getToken, setToken, removeToken } from '@/utils/token';

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(getToken());
  const user = ref<SysUser | null>(null);

  async function login(form: LoginRequest) {
    const { data: res } = await loginApi(form);
    token.value = res.data.token;
    setToken(res.data.token);
  }

  async function fetchUserInfo() {
    const { data: res } = await getCurrentUser();
    user.value = res.data;
  }

  function logout() {
    token.value = null;
    user.value = null;
    removeToken();
  }

  return { token, user, login, fetchUserInfo, logout };
});
```

### 3.5 UI 组件规范

#### 3.5.1 组件拆分原则

- 单一职责：一个组件只做一件事
- 可复用：通用逻辑抽取为公共组件
- 可组合：通过 Props 和 Events 组合使用

#### 3.5.2 组件结构

```vue
<template>
  <div class="patient-form">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
    </el-form>

    <div class="actions">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        提交
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import type { FormInstance } from 'element-plus';

interface Props {
  patient?: Patient;
}

const props = withDefaults(defineProps<Props>(), {
  patient: undefined
});

const emit = defineEmits<{
  (e: 'submit', data: Patient): void;
  (e: 'cancel'): void;
}>();

const formRef = ref<FormInstance>();
const loading = ref(false);
const form = reactive<Patient>({
  name: props.patient?.name || '',
  // ...
});

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate();
  loading.value = true;
  emit('submit', form);
  loading.value = false;
};
</script>
```

---

## 4. Git 工作流

### 4.1 分支策略

| 分支 | 用途 | 命名规则 | 生命周期 |
|------|------|----------|----------|
| main/master | 生产分支 | - | 长期 |
| develop | 开发分支 | - | 长期 |
| feature/* | 功能开发 | feature/功能名 | 临时 |
| bugfix/* | Bug 修复 | bugfix/问题描述 | 临时 |
| hotfix/* | 紧急修复 | hotfix/问题描述 | 临时 |
| release/* | 发布准备 | release/v版本号 | 临时 |

### 4.2 提交规范

#### 4.2.1 提交信息格式

```
<type>(<scope>): <subject>

[可选的正文]

[可选的脚注]
```

#### 4.2.2 Type 类型

| 类型 | 说明 | 示例 |
|------|------|------|
| feat | 新功能 | feat(patient): 添加病人搜索功能 |
| fix | Bug 修复 | fix(appointment): 修复预约时间校验问题 |
| docs | 文档更新 | docs: 更新 API 文档 |
| style | 格式调整 | style: 格式化代码 |
| refactor | 代码重构 | refactor: 优化病人查询逻辑 |
| test | 测试相关 | test: 添加病人模块单元测试 |
| chore | 构建/工具 | chore: 升级 Spring Boot 版本 |
| perf | 性能优化 | perf: 优化数据库查询 |

#### 4.2.3 提交示例

```bash
# 功能开发
git commit -m "feat(patient): 添加病人模糊搜索功能"

# Bug 修复
git commit -m "fix(appointment): 修复预约取消后状态未更新的问题"

# 文档更新
git commit -m "docs: 更新开发规范文档"

# 重构
git commit -m "refactor(service): 抽取公共方法到基类"
```

#### 4.2.4 提交注意事项

- 使用中文描述，简洁明了
- subject 不超过 50 字
- 禁止在提交信息中包含 AI 协作者相关信息
- 每次提交应该是原子性的（只包含相关的更改）
- 提交前检查代码格式

### 4.3 版本发布

#### 4.3.1 版本号规范

- **格式**：`vYY.M.N`
- **含义**：
  - `YY`：年份后两位（如 26 表示 2026 年）
  - `M`：月份（如 2 表示 2 月份）
  - `N`：当月发布的序号（从 1 开始）
- **示例**：
  - `v26.2.1` = 2026 年 2 月第 1 个版本
  - `v26.5.3` = 2026 年 5 月第 3 个版本

#### 4.3.2 发布流程

1. 从 develop 创建 release 分支：`git checkout -b release/v26.2.1`
2. 进行版本测试和修复
3. 合并到 main/master 并打标签：`git tag v26.2.1`
4. 合并回 develop
5. 删除 release 分支

#### 4.3.3 变更日志

每次发布需更新 CHANGELOG.md，记录：

```markdown
## [v26.2.1] - 2026-02-25

### 新增
- 病人管理模块完整 CRUD
- 医生管理模块完整 CRUD

### 修复
- 修复预约时间校验问题

### 优化
- 优化病人查询性能
```

---

## 5. 安全规范

### 5.1 认证授权

#### 5.1.1 认证方式

- 使用 JWT Token 进行无状态认证
- Token 存储在请求头：`Authorization: Bearer <token>`
- Token 有效期：24 小时（可配置）
- 刷新机制：Token 过期前自动刷新

#### 5.1.2 权限控制

| 角色 | 编码 | 权限范围 |
|------|------|----------|
| 管理员 | ADMIN | 全部权限 |
| 医生 | DOCTOR | 病人、病历、预约管理 |
| 护士 | NURSE | 病人、预约、药品管理 |
| 患者 | PATIENT | 预约挂号、查看自己病历 |

#### 5.1.3 后端权限注解

```java
// 方法级权限控制
@PreAuthorize("hasRole('ADMIN')")                    // 只有管理员
@PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")       // 多个角色
@PreAuthorize("hasAuthority('patient:create')")      // 细粒度权限
```

#### 5.1.4 前端路由守卫

```typescript
// router/index.ts
import { getToken } from '@/utils/token';
import { useUserStore } from '@/stores/user';

router.beforeEach(async (to, _from, next) => {
  const token = getToken();

  if (to.path === '/login') {
    token ? next({ path: '/' }) : next();
    return;
  }

  if (!token) {
    next({ path: '/login', query: { redirect: to.fullPath } });
    return;
  }

  const userStore = useUserStore();
  if (!userStore.user) {
    try {
      await userStore.fetchUserInfo();
      next();
    } catch {
      userStore.logout();
      next({ path: '/login', query: { redirect: to.fullPath } });
    }
  } else {
    next();
  }
});
```

### 5.2 数据安全

| 场景 | 安全措施 |
|------|----------|
| 密码传输 | HTTPS 加密 |
| 密码存储 | BCrypt 加密 |
| SQL 注入 | 使用参数化查询（JPA） |
| XSS | 前端转义 + 后端过滤 |
| CSRF | JWT 无状态（无需防护） |
| 敏感日志 | 禁止记录密码、身份证号等 |

---

## 6. API 文档规范

### 6.1 Swagger 集成

项目使用 **Swagger (springdoc-openapi)** 作为 API 文档工具。

#### 6.1.1 依赖配置

在 `build.gradle` 中添加依赖：

```groovy
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5'
```

#### 6.1.2 访问地址

| 工具 | 地址 |
|------|------|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |

#### 6.1.3 配置类

在 `config/SwaggerConfig.java` 中配置 API 文档信息：

```java
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HospitalInfoManagement API")
                        .version("v26.2.1")
                        .description("社区医院病人信息管理系统 REST API 文档")
                        .contact(new Contact()
                                .name("Hospital 开发组")
                                .email("laugh0608@foxmail.com")));
    }
}
```

#### 6.1.4 安全配置

在 `SecurityConfig.java` 中允许访问 API 文档相关路径：

```java
// 允许访问 Swagger UI
.ignoringRequestMatchers(
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/v3/api-docs/**"
);
```

#### 6.1.5 多版本 API 配置

项目支持同时展示 v1 和 v2 版本的 API 文档：

```properties
# application.properties 配置
springdoc.group-configs[0].group=v1
springdoc.group-configs[0].paths-to-match=/api/v1/**
springdoc.group-configs[1].group=v2
springdoc.group-configs[1].paths-to-match=/api/v2/**
```

在 Swagger UI 中可以通过下拉菜单切换不同版本的 API 文档。

---

## 7. 代码审查规范

### 7.1 审查要点

| 类别 | 审查内容 |
|------|----------|
| 功能正确性 | 业务逻辑是否正确实现 |
| 代码质量 | 命名、注释、重复代码 |
| 安全性 | 权限控制、敏感数据处理 |
| 性能 | 数据库查询、循环优化 |
| 测试 | 是否有必要的单元测试 |

### 7.2 审查清单

- [ ] 代码符合本文档规范
- [ ] 已添加必要的注释
- [ ] 已处理异常情况
- [ ] 已添加日志记录
- [ ] 已考虑性能问题
- [ ] 已测试通过

---

## 7. 文档更新记录

| 日期 | 版本 | 变更内容 | 变更人 |
|------|------|----------|--------|
| 2026-02-25 | v26.2.1 | 初始版本，创建开发规范文档 | - |
| 2026-02-26 | v26.2.3 | 更新前端项目结构、Store 划分、路由守卫示例，对齐实际代码 | - |