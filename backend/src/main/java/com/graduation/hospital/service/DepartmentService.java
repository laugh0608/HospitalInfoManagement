package com.graduation.hospital.service;

import com.graduation.hospital.entity.Department;

import java.util.List;

public interface DepartmentService {

    Department createDepartment(Department department);

    Department getDepartmentById(Long id);

    Department getDepartmentByCode(String code);

    List<Department> getAllDepartments();

    Department updateDepartment(Long id, Department department);

    void deleteDepartment(Long id);

    boolean existsByCode(String code);

    boolean existsByName(String name);
}