package com.graduation.hospital.service;

import com.graduation.hospital.entity.Department;

import java.util.List;

/**
 * 科室服务接口
 * 定义科室管理的业务操作
 */
public interface DepartmentService {

    /**
     * 创建科室
     */
    Department createDepartment(Department department);

    /**
     * 根据 ID 获取科室信息
     */
    Department getDepartmentById(Long id);

    /**
     * 根据科室编码获取科室信息
     * @param code 科室编码
     */
    Department getDepartmentByCode(String code);

    /**
     * 获取所有科室列表
     */
    List<Department> getAllDepartments();

    /**
     * 更新科室信息
     */
    Department updateDepartment(Long id, Department department);

    /**
     * 删除科室（逻辑删除）
     */
    void deleteDepartment(Long id);

    /**
     * 检查科室编码是否存在
     */
    boolean existsByCode(String code);

    /**
     * 检查科室名称是否存在
     */
    boolean existsByName(String name);
}