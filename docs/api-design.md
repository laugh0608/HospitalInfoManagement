# API 设计文档

> 文档版本：v1.0
> 最后更新：2026-02-25

---

## 1. API 设计规范

### 1.1 RESTful 风格

| 方法 | 说明 | 幂等性 |
|------|------|--------|
| GET | 查询资源 | 幂等 |
| POST | 创建资源 | 非幂等 |
| PUT | 完整更新资源 | 幂等 |
| PATCH | 部分更新资源 | 非幂等 |
| DELETE | 删除资源 | 幂等 |

### 1.2 URL 命名规范

- 资源名称使用复数形式：`/api/patients`
- 层级关系用路径表示：`/api/patients/{id}/medical-records`
- 动名词结合：`/api/appointments/{id}/confirm`

### 1.3 分页、排序、过滤

```
GET /api/patients?page=0&size=20&sort=name,asc&gender=男
```

| 参数 | 说明 | 默认值 |
|------|------|--------|
| page | 页码（从 0 开始） | 0 |
| size | 每页数量 | 20 |
| sort | 排序字段及方向 | createdAt,desc |

### 1.4 响应格式

**成功响应**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [],
    "totalElements": 100,
    "totalPages": 5,
    "page": 0,
    "size": 20
  },
  "timestamp": 1708838400000
}
```

**错误响应**：

```json
{
  "code": 404,
  "message": "资源未找到",
  "data": null,
  "timestamp": 1708838400000
}
```

### 1.5 错误码定义

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 201 | 创建成功 |
| 204 | 删除成功（无返回内容） |
| 400 | 请求参数错误 |
| 401 | 未授权（登录过期） |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 409 | 资源冲突（如重复数据） |
| 500 | 服务器内部错误 |

---

## 2. 认证接口

### 2.1 用户登录

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

### 2.2 用户登出

```
POST /api/auth/logout
```

**响应（204）**：无内容

### 2.3 获取当前用户信息

```
GET /api/auth/me
```

**响应（200）**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@hospital.com",
    "roles": ["ADMIN"],
    "permissions": ["*"]
  },
  "timestamp": 1708838400000
}
```

---

## 3. 病人管理接口

### 3.1 获取病人列表

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

**响应（200）**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "patientNo": "P20260225001",
        "name": "张三",
        "gender": "男",
        "birthDate": "1990-01-15",
        "idCard": "110101199001151234",
        "phone": "13800138000",
        "insuranceType": "城镇职工",
        "allergies": "青霉素",
        "medicalHistory": "无",
        "createdAt": "2026-02-25T10:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 5,
    "page": 0,
    "size": 20
  },
  "timestamp": 1708838400000
}
```

### 3.2 获取病人详情

```
GET /api/patients/{id}
```

**响应（200）**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "patientNo": "P20260225001",
    "name": "张三",
    "gender": "男",
    "birthDate": "1990-01-15",
    "idCard": "110101199001151234",
    "phone": "13800138000",
    "insuranceType": "城镇职工",
    "allergies": "青霉素",
    "medicalHistory": "无",
    "createdAt": "2026-02-25T10:00:00",
    "updatedAt": "2026-02-25T10:00:00"
  },
  "timestamp": 1708838400000
}
```

### 3.3 创建病人

```
POST /api/patients
```

**请求体**：

```json
{
  "name": "张三",
  "gender": "男",
  "birthDate": "1990-01-15",
  "idCard": "110101199001151234",
  "phone": "13800138000",
  "insuranceType": "城镇职工",
  "allergies": "青霉素",
  "medicalHistory": "无"
}
```

**响应（201）**：返回创建的资源

### 3.4 更新病人

```
PUT /api/patients/{id}
```

**请求体**：同创建请求，可部分更新

**响应（200）**：返回更新后的资源

### 3.5 删除病人

```
DELETE /api/patients/{id}
```

**响应（204）**：无内容

### 3.6 获取病人病历列表

```
GET /api/patients/{id}/medical-records
```

---

## 4. 医生管理接口

### 4.1 获取医生列表

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

### 4.2 获取医生详情

```
GET /api/doctors/{id}
```

### 4.3 创建医生

```
POST /api/doctors
```

**请求体**：

```json
{
  "name": "李医生",
  "title": "主任医师",
  "specialty": "心血管内科",
  "yearsOfExperience": 15,
  "departmentId": 1,
  "isAvailable": true
}
```

### 4.4 更新医生

```
PUT /api/doctors/{id}
```

### 4.5 删除医生

```
DELETE /api/doctors/{id}
```

---

## 5. 科室管理接口

### 5.1 获取科室列表

```
GET /api/departments
```

**响应（200）**：

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "内科",
      "code": "INT",
      "description": "诊治内科常见病、多发病"
    }
  ],
  "timestamp": 1708838400000
}
```

### 5.2 创建科室

```
POST /api/departments
```

### 5.3 更新科室

```
PUT /api/departments/{id}
```

### 5.4 删除科室

```
DELETE /api/departments/{id}
```

---

## 6. 预约管理接口

### 6.1 获取预约列表

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

### 6.2 获取预约详情

```
GET /api/appointments/{id}
```

### 6.3 创建预约

```
POST /api/appointments
```

**请求体**：

```json
{
  "patientId": 1,
  "doctorId": 2,
  "departmentId": 1,
  "appointmentTime": "2026-02-28T09:00:00",
  "remarks": "初诊"
}
```

### 6.4 确认预约

```
POST /api/appointments/{id}/confirm
```

### 6.5 完成预约

```
POST /api/appointments/{id}/complete
```

### 6.6 取消预约

```
POST /api/appointments/{id}/cancel
```

**请求体**：

```json
{
  "reason": "患者主动取消"
}
```

### 6.7 获取医生可用时段

```
GET /api/doctors/{id}/available-slots
```

**查询参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| date | String | 日期（yyyy-MM-dd） |

**响应（200）**：

```json
{
  "code": 200,
  "message": "success",
  "data": [
    "09:00", "09:30", "10:00", "10:30", "11:00", "14:00", "14:30", "15:00"
  ],
  "timestamp": 1708838400000
}
```

---

## 7. 病历管理接口

### 7.1 获取病历列表

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

### 7.2 获取病历详情

```
GET /api/medical-records/{id}
```

### 7.3 创建病历

```
POST /api/medical-records
```

**请求体**：

```json
{
  "patientId": 1,
  "doctorId": 2,
  "appointmentId": 10,
  "visitTime": "2026-02-28T09:30:00",
  "visitType": "门诊",
  "chiefComplaint": "头痛3天",
  "diagnosis": "偏头痛",
  "treatmentPlan": "休息，服用止痛药",
  "prescription": "布洛芬 0.2g x 6片 用法：1片/次，3次/日"
}
```

### 7.4 更新病历

```
PUT /api/medical-records/{id}
```

### 7.5 获取病人病历历史

```
GET /api/patients/{id}/medical-records
```

---

## 8. 药品管理接口

### 8.1 获取药品列表

```
GET /api/medicines
```

**查询参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| name | String | 按名称模糊搜索 |
| category | String | 按分类筛选 |
| lowStock | Boolean | 仅显示低库存药品 |

### 8.2 获取药品详情

```
GET /api/medicines/{id}
```

### 8.3 创建药品

```
POST /api/medicines
```

**请求体**：

```json
{
  "medicineCode": "MED001",
  "name": "布洛芬胶囊",
  "specification": "0.2g x 12粒/盒",
  "unit": "盒",
  "price": 15.00,
  "category": "西药",
  "minStock": 10,
  "manufacturer": "某某制药",
  "description": "解热镇痛药"
}
```

### 8.4 更新药品

```
PUT /api/medicines/{id}
```

### 8.5 删除药品

```
DELETE /api/medicines/{id}
```

### 8.6 药品入库

```
POST /api/medicines/{id}/stock-in
```

**请求体**：

```json
{
  "quantity": 100,
  "batchNo": "B20260201",
  "expiryDate": "2027-02-01",
  "supplier": "某某医药公司",
  "remarks": "首批进货"
}
```

### 8.7 药品出库

```
POST /api/medicines/{id}/stock-out
```

**请求体**：

```json
{
  "quantity": 5,
  "remarks": "处方发药"
}
```

### 8.8 获取库存记录

```
GET /api/medicines/{id}/stock-records
```

**查询参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| inOutType | String | 类型（IN/OUT/ADJUST） |
| startDate | String | 开始日期 |
| endDate | String | 结束日期 |

### 8.9 获取低库存预警列表

```
GET /api/medicines/warnings
```

---

## 9. 统计报表接口

### 9.1 数据概览

```
GET /api/statistics/overview
```

**响应（200）**：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "todayAppointments": 15,
    "pendingAppointments": 8,
    "totalPatients": 1250,
    "totalDoctors": 20,
    "lowStockMedicines": 3,
    "todayRevenue": 5680.00
  },
  "timestamp": 1708838400000
}
```

### 9.2 病人统计

```
GET /api/statistics/patients
```

**查询参数**：

| 参数 | 类型 | 说明 |
|------|------|------|
| startDate | String | 开始日期 |
| endDate | String | 结束日期 |

### 9.3 预约统计

```
GET /api/statistics/appointments
```

### 9.4 营收统计

```
GET /api/statistics/revenue
```

### 9.5 医生工作量统计

```
GET /api/statistics/doctors/workload
```

---

## 10. 用户管理接口（仅管理员）

### 10.1 获取用户列表

```
GET /api/users
```

### 10.2 获取用户详情

```
GET /api/users/{id}
```

### 10.3 创建用户

```
POST /api/users
```

**请求体**：

```json
{
  "username": "doctor1",
  "password": "password123",
  "email": "doctor1@hospital.com",
  "roleIds": [2]
}
```

### 10.4 更新用户

```
PUT /api/users/{id}
```

### 10.5 删除用户

```
DELETE /api/users/{id}
```

### 10.6 重置密码

```
POST /api/users/{id}/reset-password
```

---

## 11. 变更记录

| 日期 | 版本 | 变更内容 | 变更人 |
|------|------|----------|--------|
| 2026-02-25 | v1.0 | 初始版本，创建 API 设计文档 | - |