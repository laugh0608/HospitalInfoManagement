package com.graduation.hospital.service;

import com.graduation.hospital.entity.Patient;

import java.util.List;

public interface PatientService {

    Patient createPatient(Patient patient);

    Patient getPatientById(Long id);

    Patient getPatientByPatientNo(String patientNo);

    List<Patient> getAllPatients();

    List<Patient> searchPatients(String keyword);

    Patient updatePatient(Long id, Patient patient);

    void deletePatient(Long id);

    boolean existsByPatientNo(String patientNo);

    boolean existsByIdCard(String idCard);

    boolean existsByPhone(String phone);
}