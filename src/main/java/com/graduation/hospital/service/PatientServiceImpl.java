package com.graduation.hospital.service;

import com.graduation.hospital.common.audit.AuditLogger;
import com.graduation.hospital.entity.Patient;
import com.graduation.hospital.repository.PatientRepository;
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
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final AuditLogger auditLogger;
    private static final AtomicLong patientNoCounter = new AtomicLong(System.currentTimeMillis() % 100000);

    @Override
    @Transactional
    public Patient createPatient(Patient patient) {
        if (patientRepository.existsByIdCard(patient.getIdCard())) {
            throw new IllegalArgumentException("身份证号已存在");
        }
        if (patientRepository.existsByPhone(patient.getPhone())) {
            throw new IllegalArgumentException("手机号已存在");
        }
        patient.setPatientNo(generatePatientNo());
        Patient saved = patientRepository.save(patient);
        log.info("创建病人成功: patientNo={}, name={}", saved.getPatientNo(), saved.getName());
        auditLogger.logCreate("病人管理", "创建病人档案: " + saved.getPatientNo(), saved.getId());
        return saved;
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("病人不存在"));
    }

    @Override
    public Patient getPatientByPatientNo(String patientNo) {
        return patientRepository.findByPatientNo(patientNo)
                .orElseThrow(() -> new IllegalArgumentException("病人不存在"));
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public List<Patient> searchPatients(String keyword) {
        return patientRepository.search(keyword);
    }

    @Override
    @Transactional
    public Patient updatePatient(Long id, Patient patient) {
        Patient existing = getPatientById(id);
        if (patient.getName() != null) {
            existing.setName(patient.getName());
        }
        if (patient.getGender() != null) {
            existing.setGender(patient.getGender());
        }
        if (patient.getBirthDate() != null) {
            existing.setBirthDate(patient.getBirthDate());
        }
        if (patient.getPhone() != null) {
            existing.setPhone(patient.getPhone());
        }
        if (patient.getAddress() != null) {
            existing.setAddress(patient.getAddress());
        }
        if (patient.getInsuranceType() != null) {
            existing.setInsuranceType(patient.getInsuranceType());
        }
        if (patient.getAllergies() != null) {
            existing.setAllergies(patient.getAllergies());
        }
        if (patient.getMedicalHistory() != null) {
            existing.setMedicalHistory(patient.getMedicalHistory());
        }
        Patient updated = patientRepository.save(existing);
        log.info("更新病人信息成功: id={}", id);
        auditLogger.logUpdate("病人管理", "更新病人信息", id);
        return updated;
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("病人不存在");
        }
        patientRepository.deleteById(id);
        log.info("删除病人成功: id={}", id);
        auditLogger.logDelete("病人管理", "删除病人档案", id);
    }

    @Override
    public boolean existsByPatientNo(String patientNo) {
        return patientRepository.existsByPatientNo(patientNo);
    }

    @Override
    public boolean existsByIdCard(String idCard) {
        return patientRepository.existsByIdCard(idCard);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return patientRepository.existsByPhone(phone);
    }

    private String generatePatientNo() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long sequence = patientNoCounter.incrementAndGet();
        return "P" + date + String.format("%05d", sequence % 100000);
    }
}