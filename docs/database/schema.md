# 数据库表结构

> 文档版本：v1.0
> 最后更新：2026-02-25
> 数据库：MySQL 8.0+ / SQLite（开发）

---

## 概述

本文档详细列出系统中所有数据库表的结构。关于实体关系图和设计原则，详见 [数据库设计](design.md)。

---

## 系统表

### sys_user（系统用户表）

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
```

### sys_role（角色表）

```sql
CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
```

**内置角色**：

| code | name | description |
|------|------|-------------|
| ADMIN | 管理员 | 拥有所有权限 |
| DOCTOR | 医生 | 病人、病历、预约管理 |
| NURSE | 护士 | 病人、预约、药品管理 |
| PATIENT | 患者 | 预约挂号、查看自己病历 |

### sys_user_role（用户角色关联表）

```sql
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';
```

---

## 业务表

### department（科室表）

```sql
CREATE TABLE department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '科室名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '科室编码',
    description VARCHAR(255) COMMENT '科室描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室表';
```

### patient（病人信息表）

```sql
CREATE TABLE patient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_no VARCHAR(50) NOT NULL UNIQUE COMMENT '病人编号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) COMMENT '性别',
    birth_date DATE COMMENT '出生日期',
    id_card VARCHAR(18) COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    insurance_type VARCHAR(50) COMMENT '医保类型',
    allergies TEXT COMMENT '过敏史',
    medical_history TEXT COMMENT '既往病史',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病人信息表';
```

### doctor（医生信息表）

```sql
CREATE TABLE doctor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_no VARCHAR(50) NOT NULL UNIQUE COMMENT '医生编号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    title VARCHAR(50) COMMENT '职称',
    specialty VARCHAR(100) COMMENT '专业领域',
    years_of_experience INT COMMENT '工作年限',
    department_id BIGINT COMMENT '所属科室',
    is_available BOOLEAN DEFAULT TRUE COMMENT '是否可用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生信息表';
```

### appointment（预约挂号表）

```sql
CREATE TABLE appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_no VARCHAR(50) NOT NULL UNIQUE COMMENT '预约编号',
    patient_id BIGINT NOT NULL COMMENT '病人ID',
    doctor_id BIGINT NOT NULL COMMENT '医生ID',
    department_id BIGINT NOT NULL COMMENT '科室ID',
    appointment_time TIMESTAMP NOT NULL COMMENT '预约时间',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态',
    remarks TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约挂号表';
```

**状态枚举**：
- `PENDING` - 待确认
- `CONFIRMED` - 已确认
- `COMPLETED` - 已完成
- `CANCELLED` - 已取消

### medical_record（病历记录表）

```sql
CREATE TABLE medical_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    record_no VARCHAR(50) NOT NULL UNIQUE COMMENT '病历编号',
    patient_id BIGINT NOT NULL COMMENT '病人ID',
    doctor_id BIGINT NOT NULL COMMENT '医生ID',
    appointment_id BIGINT COMMENT '预约ID',
    visit_time TIMESTAMP NOT NULL COMMENT '就诊时间',
    visit_type VARCHAR(20) COMMENT '就诊类型',
    chief_complaint TEXT COMMENT '主诉',
    diagnosis TEXT COMMENT '诊断',
    treatment_plan TEXT COMMENT '治疗方案',
    prescription TEXT COMMENT '处方',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES appointment(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病历记录表';
```

### medicine（药品信息表）

```sql
CREATE TABLE medicine (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medicine_code VARCHAR(50) NOT NULL UNIQUE COMMENT '药品编码',
    name VARCHAR(100) NOT NULL COMMENT '药品名称',
    specification VARCHAR(100) COMMENT '规格',
    unit VARCHAR(20) COMMENT '单位',
    price DECIMAL(10,2) COMMENT '价格',
    category VARCHAR(50) COMMENT '分类',
    stock INT DEFAULT 0 COMMENT '当前库存',
    min_stock INT DEFAULT 0 COMMENT '最低库存预警值',
    manufacturer VARCHAR(100) COMMENT '生产厂家',
    description TEXT COMMENT '药品描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品信息表';
```

### medicine_stock（药品库存记录表）

```sql
CREATE TABLE medicine_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medicine_id BIGINT NOT NULL COMMENT '药品ID',
    batch_no VARCHAR(50) COMMENT '批次号',
    quantity INT NOT NULL COMMENT '数量',
    in_out_type VARCHAR(20) NOT NULL COMMENT '类型（IN/OUT/ADJUST）',
    expiry_date DATE COMMENT '有效期',
    supplier VARCHAR(100) COMMENT '供应商',
    remarks TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (medicine_id) REFERENCES medicine(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品库存记录表';
```

---

## 日志表

### db_log（数据库日志表）

```sql
CREATE TABLE db_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    level VARCHAR(20) NOT NULL COMMENT '日志级别',
    logger VARCHAR(200) NOT NULL COMMENT '日志记录器',
    message TEXT NOT NULL COMMENT '日志消息',
    trace_id VARCHAR(50) COMMENT '追踪ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据库日志表';
```

### audit_log（审计日志表）

```sql
CREATE TABLE audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) COMMENT '操作用户',
    operation VARCHAR(50) NOT NULL COMMENT '操作类型',
    method VARCHAR(10) COMMENT '请求方法',
    path VARCHAR(200) COMMENT '请求路径',
    params TEXT COMMENT '请求参数',
    result TEXT COMMENT '返回结果',
    ip VARCHAR(50) COMMENT 'IP地址',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审计日志表';
```

---

## 相关文件

| 文件 | 说明 |
|------|------|
| [数据库设计](design.md) | 数据库设计原则和 ER 图 |
| [后端架构](../architecture/backend.md) | 后端架构设计 |