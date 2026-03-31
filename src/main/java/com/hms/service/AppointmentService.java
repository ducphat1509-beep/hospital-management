package com.hms.service;

import com.hms.model.entity.Appointment;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    // Nghiệp vụ quan trọng: Đặt lịch khám
    Appointment bookAppointment(Long patientId, Long doctorId, LocalDateTime time);

    List<Appointment> getAppointmentsByDoctor(Long doctorId);
    void cancelAppointment(Long appointmentId);
}