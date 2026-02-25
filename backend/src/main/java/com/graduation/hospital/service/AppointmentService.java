package com.graduation.hospital.service;

import com.graduation.hospital.entity.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    Appointment createAppointment(Appointment appointment);

    Appointment getAppointmentById(Long id);

    Appointment getAppointmentByAppointmentNo(String appointmentNo);

    List<Appointment> getAllAppointments();

    List<Appointment> getAppointmentsByPatientId(Long patientId);

    List<Appointment> getAppointmentsByDoctorId(Long doctorId);

    List<Appointment> getAppointmentsByStatus(String status);

    Appointment updateAppointmentStatus(Long id, String status);

    Appointment cancelAppointment(Long id, String reason);

    void deleteAppointment(Long id);
}