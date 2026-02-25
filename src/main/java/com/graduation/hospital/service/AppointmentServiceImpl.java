package com.graduation.hospital.service;

import com.graduation.hospital.entity.Appointment;
import com.graduation.hospital.entity.Department;
import com.graduation.hospital.entity.Doctor;
import com.graduation.hospital.entity.Patient;
import com.graduation.hospital.repository.AppointmentRepository;
import com.graduation.hospital.repository.DepartmentRepository;
import com.graduation.hospital.repository.DoctorRepository;
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
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private static final AtomicLong appointmentNoCounter = new AtomicLong(System.currentTimeMillis() % 100000);

    @Override
    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        if (appointment.getPatient() != null && appointment.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(appointment.getPatient().getId())
                    .orElseThrow(() -> new IllegalArgumentException("病人不存在"));
            appointment.setPatient(patient);
        }
        if (appointment.getDoctor() != null && appointment.getDoctor().getId() != null) {
            Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("医生不存在"));
            appointment.setDoctor(doctor);
            if (appointment.getDepartment() == null && doctor.getDepartment() != null) {
                appointment.setDepartment(doctor.getDepartment());
            }
        }
        if (appointment.getDepartment() != null && appointment.getDepartment().getId() != null) {
            Department department = departmentRepository.findById(appointment.getDepartment().getId())
                    .orElseThrow(() -> new IllegalArgumentException("科室不存在"));
            appointment.setDepartment(department);
        }
        appointment.setAppointmentNo(generateAppointmentNo());
        if (appointment.getStatus() == null) {
            appointment.setStatus("PENDING");
        }
        Appointment saved = appointmentRepository.save(appointment);
        log.info("创建预约成功: appointmentNo={}", saved.getAppointmentNo());
        return saved;
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预约不存在"));
    }

    @Override
    public Appointment getAppointmentByAppointmentNo(String appointmentNo) {
        return appointmentRepository.findByAppointmentNo(appointmentNo)
                .orElseThrow(() -> new IllegalArgumentException("预约不存在"));
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentTimeDesc(patientId);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Appointment updateAppointmentStatus(Long id, String status) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(status);
        Appointment updated = appointmentRepository.save(appointment);
        log.info("更新预约状态: id={}, status={}", id, status);
        return updated;
    }

    @Override
    @Transactional
    public Appointment cancelAppointment(Long id, String reason) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus("CANCELLED");
        appointment.setCancelTime(LocalDateTime.now());
        appointment.setCancelReason(reason);
        Appointment updated = appointmentRepository.save(appointment);
        log.info("取消预约: id={}, reason={}", id, reason);
        return updated;
    }

    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("预约不存在");
        }
        appointmentRepository.deleteById(id);
        log.info("删除预约成功: id={}", id);
    }

    private String generateAppointmentNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        long sequence = appointmentNoCounter.incrementAndGet();
        return "APT" + date + String.format("%04d", sequence % 10000);
    }
}