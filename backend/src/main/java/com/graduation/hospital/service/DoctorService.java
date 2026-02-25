package com.graduation.hospital.service;

import com.graduation.hospital.entity.Doctor;

import java.util.List;

public interface DoctorService {

    Doctor createDoctor(Doctor doctor);

    Doctor getDoctorById(Long id);

    Doctor getDoctorByDoctorNo(String doctorNo);

    List<Doctor> getAllDoctors();

    List<Doctor> getDoctorsByDepartmentId(Long departmentId);

    List<Doctor> getAvailableDoctors();

    Doctor updateDoctor(Long id, Doctor doctor);

    void deleteDoctor(Long id);

    void setAvailable(Long id, boolean available);
}