package com.graduation.hospital.controller;

import com.graduation.hospital.common.Result;
import com.graduation.hospital.entity.Department;
import com.graduation.hospital.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 科室信息管理控制器
 * 提供科室的 CRUD 操作接口
 */
@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * 创建科室
     * 权限：仅管理员
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Department> createDepartment(@RequestBody Department department) {
        Department saved = departmentService.createDepartment(department);
        return Result.success(saved);
    }

    /**
     * 根据 ID 获取科室信息
     */
    @GetMapping("/{id}")
    public Result<Department> getDepartmentById(@PathVariable Long id) {
        return Result.success(departmentService.getDepartmentById(id));
    }

    /**
     * 根据科室编码获取科室信息
     * @param code 科室编码（如：InternalMedicine）
     */
    @GetMapping("/code/{code}")
    public Result<Department> getDepartmentByCode(@PathVariable String code) {
        return Result.success(departmentService.getDepartmentByCode(code));
    }

    /**
     * 获取所有科室列表
     */
    @GetMapping
    public Result<List<Department>> getAllDepartments() {
        return Result.success(departmentService.getAllDepartments());
    }

    /**
     * 更新科室信息
     * 权限：仅管理员
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return Result.success(departmentService.updateDepartment(id, department));
    }

    /**
     * 删除科室（逻辑删除）
     * 权限：仅管理员
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return Result.success();
    }
}