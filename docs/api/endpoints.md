# API 接口清单

> 文档版本：v1.0
> 最后更新：2026-02-25

---

## 1. 认证接口

### 1.1 用户登录

```
POST /api/auth/login
```

**请求体**：

```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应（200）**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "admin",
    "roles": ["ADMIN"],
    "expiresIn": 86400
  },
  "timestamp": 1708838400000
}
```

### 1.2 用户登出

```
POST /api/auth/logout
```

**响应（204）**：无内容

### 1.3 获取当前用户信息

```
GET /api/auth/me
```

---

## 2. 病人管理接口

### 2.1 获取病人列表

```
GET /api/patients
```

**查询参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| name | String | 按姓名模糊搜索 |
| phone | String | 按电话精确查询 |
| idCard | String | 按身份证号精确查询 |
| page | Integer | 页码 |
| size | Integer | 每页数量 |
| sort | String | 排序 |

### 2.2 获取病人详情

```
GET /api/patients/{id}
```

### 2.3 创建病人

```
POST /api/patients
```

### 2.4 更新病人

```
PUT /api/patients/{id}
```

### 2.5 删除病人

```
DELETE /api/patients/{id}
```

### 2.6 获取病人病历列表

```
GET /api/patients/{id}/medical-records
```

---

## 3. 医生管理接口

### 3.1 获取医生列表

```
GET /api/doctors
```

**查询参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| name | String | 按姓名模糊搜索 |
| departmentId | Long | 按科室筛选 |
| title | String | 按职称筛选 |
| isAvailable | Boolean | 筛选可用状态 |

### 3.2 获取医生详情

```
GET /api/doctors/{id}
```

### 3.3 创建医生

```
POST /api/doctors
```

### 3.4 更新医生

```
PUT /api/doctors/{id}
```

### 3.5 删除医生

```
DELETE /api/doctors/{id}
```

---

## 4. 科室管理接口

### 4.1 获取科室列表

```
GET /api/departments
```

### 4.2 创建科室

```
POST /api/departments
```

### 4.3 更新科室

```
PUT /api/departments/{id}
```

### 4.4 删除科室

```
DELETE /api/departments/{id}
```

---

## 5. 预约管理接口

### 5.1 获取预约列表

```
GET /api/appointments
```

**查询参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| patientId | Long | 按病人筛选 |
| doctorId | Long | 按医生筛选 |
| departmentId | Long | 按科室筛选 |
| status | String | 按状态筛选（PENDING/CONFIRMED/COMPLETED/CANCELLED） |
| startDate | String | 预约开始日期 |
| endDate | String | 预约结束日期 |

### 5.2 获取预约详情

```
GET /api/appointments/{id}
```

### 5.3 创建预约

```
POST /api/appointments
```

### 5.4 确认预约

```
POST /api/appointments/{id}/confirm
```

### 5.5 完成预约

```
POST /api/appointments/{id}/complete
```

### 5.6 取消预约

```
POST /api/appointments/{id}/cancel
```

### 5.7 获取医生可用时段

```
GET /api/doctors/{id}/available-slots
```

---

## 6. 病历管理接口

### 6.1 获取病历列表

```
GET /api/medical-records
```

**查询参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| patientId | Long | 按病人筛选 |
| doctorId | Long | 按医生筛选 |
| visitType | String | 按就诊类型筛选 |
| startDate | String | 就诊开始日期 |
| endDate | String | 就诊结束日期 |

### 6.2 获取病历详情

```
GET /api/medical-records/{id}
```

### 6.3 创建病历

```
POST /api/medical-records
```

### 6.4 更新病历

```
PUT /api/medical-records/{id}
```

### 6.5 获取病人病历历史

```
GET /api/patients/{id}/medical-records
```

---

## 7. 药品管理接口

### 7.1 获取药品列表

```
GET /api/medicines
```

**查询参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| name | String | 按名称模糊搜索 |
| category | String | 按分类筛选 |
| lowStock | Boolean | 仅显示低库存药品 |

### 7.2 获取药品详情

```
GET /api/medicines/{id}
```

### 7.3 创建药品

```
POST /api/medicines
```

### 7.4 更新药品

```
PUT /api/medicines/{id}
```

### 7.5 删除药品

```
DELETE /api/medicines/{id}
```

### 7.6 药品入库

```
POST /api/medicines/{id}/stock-in
```

### 7.7 药品出库

```
POST /api/medicines/{id}/stock-out
```

### 7.8 获取库存记录

```
GET /api/medicines/{id}/stock-records
```

### 7.9 获取低库存预警列表

```
GET /api/medicines/warnings
```

---

## 8. 统计报表接口

### 8.1 数据概览

```
GET /api/statistics/overview
```

### 8.2 病人统计

```
GET /api/statistics/patients
```

### 8.3 预约统计

```
GET /api/statistics/appointments
```

### 8.4 营收统计

```
GET /api/statistics/revenue
```

### 8.5 医生工作量统计

```
GET /api/statistics/doctors/workload
```

---

## 9. 用户管理接口（仅管理员）

### 9.1 获取用户列表

```
GET /api/users
```

### 9.2 获取用户详情

```
GET /api/users/{id}
```

### 9.3 创建用户

```
POST /api/users
```

### 9.4 更新用户

```
PUT /api/users/{id}
```

### 9.5 删除用户

```
DELETE /api/users/{id}
```

### 9.6 重置密码

```
POST /api/users/{id}/reset-password
```

---

## 相关文件

| 文件 | 说明 |
|------|------|
| [API 设计规范](overview.md) | API 设计原则和约定 |
| [后端架构](../architecture/backend.md) | 后端架构设计 |