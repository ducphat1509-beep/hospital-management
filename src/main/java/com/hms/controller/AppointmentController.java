package com.hms.controller;

import com.hms.model.entity.Appointment;
import com.hms.model.entity.Doctor;
import com.hms.model.entity.Patient;
import com.hms.service.AppointmentService;
import com.hms.service.DoctorService;
import com.hms.service.PatientService;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public List<Patient> getPatients() { return patientService.getAllPatients(); }
    public List<Doctor> getDoctors() { return doctorService.getAllDoctors(); }

    public void createAppointment(Long pId, Long dId, LocalDateTime time) {
        appointmentService.bookAppointment(pId, dId, time);
    }

    public List<Appointment> getAllAppointments() {
        // Tạm thời dùng hàm findAll của DAO thông qua Service
        return appointmentService.getAppointmentsByDoctor(null);
    }

    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentService.getAppointmentsByDoctor(doctorId);
    }
}