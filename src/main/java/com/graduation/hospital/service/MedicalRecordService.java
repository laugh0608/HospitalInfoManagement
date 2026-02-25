package com.graduation.hospital.service;

import com.graduation.hospital.entity.MedicalRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalRecordService {

    MedicalRecord createMedicalRecord(MedicalRecord medicalRecord);

    MedicalRecord getMedicalRecordById(Long id);

    MedicalRecord getMedicalRecordByRecordNo(String recordNo);

    List<MedicalRecord> getAllMedicalRecords();

    List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId);

    List<MedicalRecord> getMedicalRecordsByDoctorId(Long doctorId);

    List<MedicalRecord> getMedicalRecordsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    MedicalRecord updateMedicalRecord(Long id, MedicalRecord medicalRecord);

    void deleteMedicalRecord(Long id);
}