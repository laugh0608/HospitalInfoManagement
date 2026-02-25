package com.graduation.hospital.service;

import com.graduation.hospital.entity.Doctor;
import com.graduation.hospital.entity.MedicalRecord;
import com.graduation.hospital.entity.Patient;
import com.graduation.hospital.repository.DoctorRepository;
import com.graduation.hospital.repository.MedicalRecordRepository;
import com.graduation.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private static final AtomicLong recordNoCounter = new AtomicLong(System.currentTimeMillis() % 100000);

    @Override
    @Transactional
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        if (medicalRecord.getPatient() != null && medicalRecord.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(medicalRecord.getPatient().getId())
                    .orElseThrow(() -> new IllegalArgumentException("病人不存在"));
            medicalRecord.setPatient(patient);
        }
        if (medicalRecord.getDoctor() != null && medicalRecord.getDoctor().getId() != null) {
            Doctor doctor = doctorRepository.findById(medicalRecord.getDoctor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("医生不存在"));
            medicalRecord.setDoctor(doctor);
        }
        medicalRecord.setRecordNo(generateRecordNo());
        if (medicalRecord.getVisitTime() == null) {
            medicalRecord.setVisitTime(LocalDateTime.now());
        }
        MedicalRecord saved = medicalRecordRepository.save(medicalRecord);
        log.info("创建病历成功: recordNo={}", saved.getRecordNo());
        return saved;
    }

    @Override
    public MedicalRecord getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("病历不存在"));
    }

    @Override
    public MedicalRecord getMedicalRecordByRecordNo(String recordNo) {
        return medicalRecordRepository.findByRecordNo(recordNo)
                .orElseThrow(() -> new IllegalArgumentException("病历不存在"));
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientIdOrderByVisitTimeDesc(patientId);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByDoctorId(Long doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return medicalRecordRepository.findByVisitTimeBetween(startTime, endTime);
    }

    @Override
    @Transactional
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord medicalRecord) {
        MedicalRecord existing = getMedicalRecordById(id);
        if (medicalRecord.getVisitType() != null) {
            existing.setVisitType(medicalRecord.getVisitType());
        }
        if (medicalRecord.getChiefComplaint() != null) {
            existing.setChiefComplaint(medicalRecord.getChiefComplaint());
        }
        if (medicalRecord.getDiagnosis() != null) {
            existing.setDiagnosis(medicalRecord.getDiagnosis());
        }
        if (medicalRecord.getTreatmentPlan() != null) {
            existing.setTreatmentPlan(medicalRecord.getTreatmentPlan());
        }
        if (medicalRecord.getPrescription() != null) {
            existing.setPrescription(medicalRecord.getPrescription());
        }
        if (medicalRecord.getRemarks() != null) {
            existing.setRemarks(medicalRecord.getRemarks());
        }
        if (medicalRecord.getFollowUpDate() != null) {
            existing.setFollowUpDate(medicalRecord.getFollowUpDate());
        }
        MedicalRecord updated = medicalRecordRepository.save(existing);
        log.info("更新病历成功: id={}", id);
        return updated;
    }

    @Override
    @Transactional
    public void deleteMedicalRecord(Long id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new IllegalArgumentException("病历不存在");
        }
        medicalRecordRepository.deleteById(id);
        log.info("删除病历成功: id={}", id);
    }

    private String generateRecordNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        long sequence = recordNoCounter.incrementAndGet();
        return "MR" + date + String.format("%04d", sequence % 10000);
    }
}