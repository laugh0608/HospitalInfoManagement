# 数据库设计文档

> 文档版本：v1.0
> 最后更新：2026-02-25
> 数据库：MySQL 8.0+ / SQLite（开发）

---

## 1. 数据库概述

### 1.1 设计原则

- **命名规范**：表名使用 `snake_case`，字段名使用 `camelCase`
- **主键策略**：所有表使用 `Long` 类型自增主键 `id`
- **时间戳**：创建时间 `createdAt`、更新时间 `updatedAt` 使用 `LocalDateTime`
- **软删除**：暂不启用物理删除，通过 `status` 或 `deleted` 字段标记

### 1.2 实体关系图

```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│    sys_user     │       │    sys_role     │       │   sys_permission│
├─────────────────┤       ├─────────────────┤       ├─────────────────┤
│ id (PK)         │◄──────│ id (PK)         │       │ id (PK)         │
│ username        │       │ name            │       │ name            │
│ password        │       │ code            │◄──────│ code            │
│ email           │       │ description     │       │ description     │
│ enabled         │       │ created_at      │       │ created_at      │
│ created_at      │       │ updated_at      │       │ updated_at      │
│ updated_at      │       └────────┬────────┘       └─────────────────┘
└────────┬────────┘                │
         │                           │
         │ 1:N                       │ N:N
         ▼                           ▼
┌─────────────────┐       ┌─────────────────┐
│     doctor      │       │    patient      │
├─────────────────┤       ├─────────────────┤
│ id (PK)         │       │ id (PK)         │
│ user_id (FK)    │       │ patient_no (UK) │
│ doctor_no (UK)  │       │ name            │
│ name            │       │ gender          │
│ title           │       │ birth_date      │
│ specialty       │       │ id_card         │
│ years_experience│       │ phone           │
│ department_id   │       │ insurance_type  │
│ is_available    │       │ allergies       │
│ created_at      │       │ medical_history │
│ updated_at      │       │ created_at      │
└────────┬────────┘       │ updated_at      │
         │                 └────────┬────────┘
         │                          │
         │ N:1                      │ 1:N
         ▼                          ▼
┌─────────────────┐       ┌─────────────────┐
│   department    │       │   appointment   │
├─────────────────┤       ├─────────────────┤
│ id (PK)         │       │ id (PK)         │
│ name            │       │ appointment_no  │
│ code (UK)       │       │ patient_id (FK) │
│ description     │       │ doctor_id (FK)  │
│ created_at      │       │ department_id   │
│ updated_at      │       │ appointment_time │
└─────────────────┘       │ status          │
                          │ created_at      │
                          │ updated_at      │
                          └────────┬────────┘
                                   │
         ┌─────────────────────────┼─────────────────────────┐
         │                         │                         │
         ▼                         ▼                         ▼
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│  medical_record │       │    medicine     │       │  medicine_stock │
├─────────────────┤       ├─────────────────┤       ├─────────────────┤
│ id (PK)         │       │ id (PK)         │       │ id (PK)         │
│ record_no (UK)  │       │ medicine_code   │       │ medicine_id     │
│ patient_id (FK) │       │ name            │       │ batch_no        │
│ doctor_id (FK)  │       │ specification   │       │ quantity        │
│ visit_time      │       │ price           │       │ expiry_date     │
│ visit_type      │       │ category        │       │ in_out_type     │
│ chief_complaint │       │ stock           │       │ created_at      │
│ diagnosis       │       │ min_stock       │       │ updated_at      │
│ treatment_plan │       │ created_at      │       └─────────────────┘
│ created_at      │       │ updated_at      │
│ updated_at      │       └─────────────────┘
└─────────────────┘
```

---

## 2. 表结构详细设计

### 2.1 系统用户表（sys_user）

系统用户表用于存储系统登录用户信息。

```sql
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    email VARCHAR(100) COMMENT '邮箱',
    enabled BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

CREATE INDEX idx_username ON sys_user(username);
```

**字段说明**：

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| username | VARCHAR(50) | NOT NULL, UNIQUE | 用户名 |
| password | VARCHAR(255) | NOT NULL | BCrypt 加密后的密码 |
| email | VARCHAR(100) | NULL | 邮箱地址 |
| enabled | BOOLEAN | DEFAULT TRUE | 账户是否启用 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

---

### 2.2 角色表（sys_role）

```sql
CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码（ADMIN/DOCTOR/NURSE/PATIENT）',
    description VARCHAR(255) COMMENT '角色描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE INDEX idx_role_code ON sys_role(code);
```

**内置角色**：

| code | name | description |
|------|------|-------------|
| ADMIN | 管理员 | 拥有所有权限 |
| DOCTOR | 医生 | 病人、病历、预约管理 |
| NURSE | 护士 | 病人、预约、药品管理 |
| PATIENT | 患者 | 预约挂号、查看自己病历 |

---

### 2.3 用户角色关联表（sys_user_role）

```sql
CREATE TABLE sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';
```

---

### 2.4 科室表（department）

```sql
CREATE TABLE department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '科室名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '科室编码',
    description VARCHAR(255) COMMENT '科室描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室表';

CREATE INDEX idx_dept_code ON department(code);
```

**初始化数据**：

| code | name | description |
|------|------|-------------|
| INT | 内科 | 诊治内科常见病、多发病 |
| SUR | 外科 | 诊治外科疾病 |
| GYN | 妇科 | 诊治妇科疾病 |
| PED | 儿科 | 诊治儿童疾病 |
| DEN | 口腔科 | 口腔疾病诊治 |
| EYE | 眼科 | 眼部疾病诊治 |
| ENT | 耳鼻喉科 | 耳鼻喉疾病诊治 |
| TCM | 中医科 | 中医诊疗 |

---

### 2.5 医生信息表（doctor）

医生信息表与系统用户表是一对一关系。

```sql
CREATE TABLE doctor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '关联系统用户ID',
    doctor_no VARCHAR(50) NOT NULL UNIQUE COMMENT '医生编号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    title VARCHAR(50) COMMENT '职称（主任医师/副主任医师/主治医师/住院医师）',
    specialty VARCHAR(100) COMMENT '专业领域',
    years_of_experience INT DEFAULT 0 COMMENT '工作年限',
    department_id BIGINT NOT NULL COMMENT '所属科室ID',
    is_available BOOLEAN DEFAULT TRUE COMMENT '是否可用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE SET NULL,
    FOREIGN KEY (department_id) REFERENCES department(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生信息表';

CREATE INDEX idx_doctor_no ON doctor(doctor_no);
CREATE INDEX idx_doctor_dept ON doctor(department_id);
```

---

### 2.6 病人信息表（patient）

```sql
CREATE TABLE patient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_no VARCHAR(50) NOT NULL UNIQUE COMMENT '病人编号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) NOT NULL COMMENT '性别（男/女）',
    birth_date DATE COMMENT '出生日期',
    id_card VARCHAR(18) COMMENT '身份证号',
    phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    insurance_type VARCHAR(50) COMMENT '医保类型（城镇职工/城镇居民/新农合/自费）',
    allergies TEXT COMMENT '过敏史',
    medical_history TEXT COMMENT '既往病史',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病人信息表';

CREATE INDEX idx_patient_no ON patient(patient_no);
CREATE INDEX idx_patient_phone ON patient(phone);
CREATE INDEX idx_patient_name ON patient(name);
```

---

### 2.7 预约挂号表（appointment）

```sql
CREATE TABLE appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_no VARCHAR(50) NOT NULL UNIQUE COMMENT '预约编号',
    patient_id BIGINT NOT NULL COMMENT '病人ID',
    doctor_id BIGINT NOT NULL COMMENT '医生ID',
    department_id BIGINT NOT NULL COMMENT '科室ID',
    appointment_time TIMESTAMP NOT NULL COMMENT '预约时间',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态（PENDING/CONFIRMED/COMPLETED/CANCELLED）',
    remarks TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    FOREIGN KEY (department_id) REFERENCES department(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约挂号表';

CREATE INDEX idx_appointment_no ON appointment(appointment_no);
CREATE INDEX idx_appointment_patient ON appointment(patient_id);
CREATE INDEX idx_appointment_doctor ON appointment(doctor_id);
CREATE INDEX idx_appointment_time ON appointment(appointment_time);
CREATE INDEX idx_appointment_status ON appointment(status);
```

**状态说明**：

| status | 说明 |
|--------|------|
| PENDING | 待确认 |
| CONFIRMED | 已确认 |
| COMPLETED | 已完成 |
| CANCELLED | 已取消 |

---

### 2.8 病历记录表（medical_record）

```sql
CREATE TABLE medical_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    record_no VARCHAR(50) NOT NULL UNIQUE COMMENT '病历编号',
    patient_id BIGINT NOT NULL COMMENT '病人ID',
    doctor_id BIGINT NOT NULL COMMENT '医生ID',
    appointment_id BIGINT COMMENT '关联预约ID',
    visit_time TIMESTAMP NOT NULL COMMENT '就诊时间',
    visit_type VARCHAR(20) NOT NULL COMMENT '就诊类型（门诊/急诊/住院）',
    chief_complaint TEXT COMMENT '主诉',
    diagnosis TEXT COMMENT '诊断',
    treatment_plan TEXT COMMENT '治疗方案',
    prescription TEXT COMMENT '处方',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    FOREIGN KEY (appointment_id) REFERENCES appointment(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病历记录表';

CREATE INDEX idx_record_no ON medical_record(record_no);
CREATE INDEX idx_record_patient ON medical_record(patient_id);
CREATE INDEX idx_record_doctor ON medical_record(doctor_id);
CREATE INDEX idx_record_visit_time ON medical_record(visit_time);
```

---

### 2.9 药品信息表（medicine）

```sql
CREATE TABLE medicine (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medicine_code VARCHAR(50) NOT NULL UNIQUE COMMENT '药品编码',
    name VARCHAR(100) NOT NULL COMMENT '药品名称',
    specification VARCHAR(100) COMMENT '规格',
    unit VARCHAR(20) COMMENT '单位（盒/瓶/支）',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    category VARCHAR(20) NOT NULL COMMENT '分类（西药/中成药/中药）',
    stock INT DEFAULT 0 COMMENT '当前库存',
    min_stock INT DEFAULT 10 COMMENT '最低库存预警值',
    manufacturer VARCHAR(100) COMMENT '生产厂家',
    description TEXT COMMENT '药品说明',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品信息表';

CREATE INDEX idx_medicine_code ON medicine(medicine_code);
CREATE INDEX idx_medicine_name ON medicine(name);
CREATE INDEX idx_medicine_category ON medicine(category);
```

**分类说明**：

| category | 说明 |
|----------|------|
| 西药 | 化学药品 |
| 中成药 | 中成药制剂 |
| 中药 | 中药材/饮片 |

---

### 2.10 药品库存记录表（medicine_stock）

用于记录药品的出入库历史。

```sql
CREATE TABLE medicine_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medicine_id BIGINT NOT NULL COMMENT '药品ID',
    batch_no VARCHAR(50) COMMENT '批号',
    quantity INT NOT NULL COMMENT '数量（正数入库/负数出库）',
    in_out_type VARCHAR(20) NOT NULL COMMENT '类型（IN入库/OUT出库/ADJUST调整）',
    expiry_date DATE COMMENT '有效期',
    supplier VARCHAR(100) COMMENT '供应商',
    remarks TEXT COMMENT '备注',
    operator_id BIGINT COMMENT '操作人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (medicine_id) REFERENCES medicine(id) ON DELETE CASCADE,
    FOREIGN KEY (operator_id) REFERENCES sys_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品库存记录表';

CREATE INDEX idx_stock_medicine ON medicine_stock(medicine_id);
CREATE INDEX idx_stock_batch ON medicine_stock(batch_no);
CREATE INDEX idx_stock_time ON medicine_stock(created_at);
```

---

## 3. 索引设计汇总

| 表名 | 索引名 | 字段 | 类型 |
|------|--------|------|------|
| sys_user | idx_username | username | UNIQUE |
| sys_role | idx_role_code | code | UNIQUE |
| department | idx_dept_code | code | UNIQUE |
| doctor | idx_doctor_no | doctor_no | UNIQUE |
| doctor | idx_doctor_dept | department_id | INDEX |
| patient | idx_patient_no | patient_no | UNIQUE |
| patient | idx_patient_phone | phone | INDEX |
| patient | idx_patient_name | name | INDEX |
| appointment | idx_appointment_no | appointment_no | UNIQUE |
| appointment | idx_appointment_patient | patient_id | INDEX |
| appointment | idx_appointment_doctor | doctor_id | INDEX |
| appointment | idx_appointment_time | appointment_time | INDEX |
| appointment | idx_appointment_status | status | INDEX |
| medical_record | idx_record_no | record_no | UNIQUE |
| medical_record | idx_record_patient | patient_id | INDEX |
| medical_record | idx_record_doctor | doctor_id | INDEX |
| medical_record | idx_record_visit_time | visit_time | INDEX |
| medicine | idx_medicine_code | medicine_code | UNIQUE |
| medicine | idx_medicine_name | name | INDEX |
| medicine | idx_medicine_category | category | INDEX |
| medicine_stock | idx_stock_medicine | medicine_id | INDEX |
| medicine_stock | idx_stock_batch | batch_no | INDEX |
| medicine_stock | idx_stock_time | created_at | INDEX |

---

## 4. 初始化数据

### 4.1 角色初始化

```sql
INSERT INTO sys_role (name, code, description) VALUES
('管理员', 'ADMIN', '拥有系统所有权限'),
('医生', 'DOCTOR', '病人、病历、预约管理'),
('护士', 'NURSE', '病人、预约、药品管理'),
('患者', 'PATIENT', '预约挂号、查看自己病历');
```

### 4.2 科室初始化

```sql
INSERT INTO department (name, code, description) VALUES
('内科', 'INT', '诊治内科常见病、多发病'),
('外科', 'SUR', '诊治外科疾病'),
('妇科', 'GYN', '诊治妇科疾病'),
('儿科', 'PED', '诊治儿童疾病'),
('口腔科', 'DEN', '口腔疾病诊治'),
('眼科', 'EYE', '眼部疾病诊治'),
('耳鼻喉科', 'ENT', '耳鼻喉疾病诊治'),
('中医科', 'TCM', '中医诊疗');
```

### 4.3 测试用户

默认管理员账户（密码均为 `admin123`）：

```sql
-- 管理员用户
INSERT INTO sys_user (username, password, email, enabled) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'admin@hospital.com', TRUE);

-- 获取管理员ID并分配角色
SET @admin_id = LAST_INSERT_ID();
INSERT INTO sys_user_role (user_id, role_id) VALUES (@admin_id, 1);
```

**说明**：`$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH` 是 `admin123` 的 BCrypt 哈希值。

---

## 5. 变更记录

| 日期 | 版本 | 变更内容 | 变更人 |
|------|------|----------|--------|
| 2026-02-25 | v1.0 | 初始版本，创建数据库设计文档 | - |