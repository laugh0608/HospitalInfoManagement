package com.graduation.hospital.service;

import com.graduation.hospital.common.audit.AuditLogger;
import com.graduation.hospital.entity.Department;
import com.graduation.hospital.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final AuditLogger auditLogger;

    @Override
    @Transactional
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByCode(department.getCode())) {
            throw new IllegalArgumentException("科室编码已存在");
        }
        if (departmentRepository.existsByName(department.getName())) {
            throw new IllegalArgumentException("科室名称已存在");
        }
        Department saved = departmentRepository.save(department);
        log.info("创建科室成功: code={}, name={}", saved.getCode(), saved.getName());
        auditLogger.logCreate("科室管理", "创建科室: " + saved.getCode(), saved.getId());
        return saved;
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("科室不存在"));
    }

    @Override
    public Department getDepartmentByCode(String code) {
        return departmentRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("科室不存在"));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional
    public Department updateDepartment(Long id, Department department) {
        Department existing = getDepartmentById(id);
        if (department.getName() != null) {
            existing.setName(department.getName());
        }
        if (department.getDescription() != null) {
            existing.setDescription(department.getDescription());
        }
        if (department.getLocation() != null) {
            existing.setLocation(department.getLocation());
        }
        if (department.getPhone() != null) {
            existing.setPhone(department.getPhone());
        }
        Department updated = departmentRepository.save(existing);
        log.info("更新科室信息成功: id={}", id);
        auditLogger.logUpdate("科室管理", "更新科室信息", id);
        return updated;
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new IllegalArgumentException("科室不存在");
        }
        departmentRepository.deleteById(id);
        log.info("删除科室成功: id={}", id);
        auditLogger.logDelete("科室管理", "删除科室", id);
    }

    @Override
    public boolean existsByCode(String code) {
        return departmentRepository.existsByCode(code);
    }

    @Override
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }
}