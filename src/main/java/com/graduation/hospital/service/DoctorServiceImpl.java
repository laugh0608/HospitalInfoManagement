package com.graduation.hospital.service;

import com.graduation.hospital.common.audit.AuditLogger;
import com.graduation.hospital.entity.Department;
import com.graduation.hospital.entity.Doctor;
import com.graduation.hospital.repository.DepartmentRepository;
import com.graduation.hospital.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final AuditLogger auditLogger;
    private static final AtomicLong doctorNoCounter = new AtomicLong(System.currentTimeMillis() % 10000);

    @Override
    @Transactional
    public Doctor createDoctor(Doctor doctor) {
        if (doctor.getDepartment() != null && doctor.getDepartment().getId() != null) {
            Department department = departmentRepository.findById(doctor.getDepartment().getId())
                    .orElseThrow(() -> new IllegalArgumentException("科室不存在"));
            doctor.setDepartment(department);
        }
        doctor.setDoctorNo(generateDoctorNo());
        doctor.setIsAvailable(true);
        Doctor saved = doctorRepository.save(doctor);
        log.info("创建医生成功: doctorNo={}, name={}", saved.getDoctorNo(), saved.getName());
        auditLogger.logCreate("医生管理", "创建医生: " + saved.getDoctorNo(), saved.getId());
        return saved;
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("医生不存在"));
    }

    @Override
    public Doctor getDoctorByDoctorNo(String doctorNo) {
        return doctorRepository.findByDoctorNo(doctorNo)
                .orElseThrow(() -> new IllegalArgumentException("医生不存在"));
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public List<Doctor> getDoctorsByDepartmentId(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId);
    }

    @Override
    public List<Doctor> getAvailableDoctors() {
        return doctorRepository.findByIsAvailableTrue();
    }

    @Override
    @Transactional
    public Doctor updateDoctor(Long id, Doctor doctor) {
        Doctor existing = getDoctorById(id);
        if (doctor.getName() != null) {
            existing.setName(doctor.getName());
        }
        if (doctor.getTitle() != null) {
            existing.setTitle(doctor.getTitle());
        }
        if (doctor.getSpecialty() != null) {
            existing.setSpecialty(doctor.getSpecialty());
        }
        if (doctor.getYearsOfExperience() != null) {
            existing.setYearsOfExperience(doctor.getYearsOfExperience());
        }
        if (doctor.getDepartment() != null && doctor.getDepartment().getId() != null) {
            Department department = departmentRepository.findById(doctor.getDepartment().getId())
                    .orElseThrow(() -> new IllegalArgumentException("科室不存在"));
            existing.setDepartment(department);
        }
        if (doctor.getIntroduction() != null) {
            existing.setIntroduction(doctor.getIntroduction());
        }
        Doctor updated = doctorRepository.save(existing);
        log.info("更新医生信息成功: id={}", id);
        auditLogger.logUpdate("医生管理", "更新医生信息", id);
        return updated;
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new IllegalArgumentException("医生不存在");
        }
        doctorRepository.deleteById(id);
        log.info("删除医生成功: id={}", id);
        auditLogger.logDelete("医生管理", "删除医生", id);
    }

    @Override
    @Transactional
    public void setAvailable(Long id, boolean available) {
        Doctor doctor = getDoctorById(id);
        doctor.setIsAvailable(available);
        doctorRepository.save(doctor);
        log.info("设置医生可用状态: id={}, available={}", id, available);
    }

    private String generateDoctorNo() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        long sequence = doctorNoCounter.incrementAndGet();
        return "D" + date + String.format("%04d", sequence % 10000);
    }
}