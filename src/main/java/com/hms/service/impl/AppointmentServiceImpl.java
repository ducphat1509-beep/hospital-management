package com.hms.service.impl;

import com.hms.config.JpaUtil;
import com.hms.dao.AppointmentDAO;
import com.hms.dao.DoctorDAO;
import com.hms.dao.PatientDAO;
import com.hms.model.entity.Appointment;
import com.hms.model.entity.Doctor;
import com.hms.model.entity.Patient;
import com.hms.service.AppointmentService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    // Service này cần cả 3 "tay chân" để làm việc
    private final AppointmentDAO appointmentDAO;
    private final DoctorDAO doctorDAO;
    private final PatientDAO patientDAO;

    @Override
    public Appointment bookAppointment(Long patientId, Long doctorId, LocalDateTime time) {
        // 1. Kiểm tra thời gian (Không được đặt lịch trong quá khứ)
        if (time.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Thời gian hẹn không thể ở quá khứ!");
        }

        // 2. Tìm bệnh nhân - Nếu không thấy thì báo lỗi ngay
        Patient patient = patientDAO.findById(patientId);
        if (patient == null) {
            throw new RuntimeException("Bệnh nhân không tồn tại!");
        }

        // 3. Tìm bác sĩ
        Doctor doctor = doctorDAO.findById(doctorId);
        if (doctor == null) {
            throw new RuntimeException("Bác sĩ không tồn tại!");
        }

        // 4. Tạo đối tượng lịch hẹn và gán dữ liệu
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(time);
        appointment.setStatus(Appointment.AppointmentStatus.PENDING);

        // 5. Lưu vào DB thông qua DAO
        appointmentDAO.save(appointment);

        return appointment;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            if (doctorId == null) {
                // Nếu không chọn bác sĩ nào, lấy tất cả
                return em.createQuery("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor ORDER BY a.status ASC, a.appointmentTime ASC", Appointment.class).getResultList();
            }
            // Lọc theo doctorId
            return em.createQuery("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor WHERE a.doctor.id = :dId ORDER BY a.status ASC, a.appointmentTime ASC", Appointment.class)
                    .setParameter("dId", doctorId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void updateStatus(Long appointmentId, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(status);
            appointmentDAO.save(appointment);
        } else {
            throw new RuntimeException("Không tìm thấy lịch hẹn với ID: " + appointmentId);
        }
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        // Nghiệp vụ hủy lịch: Có thể không xóa hẳn mà chỉ đổi status thành CANCELLED
        // Nhưng ở mức đơn giản, ta gọi delete
        appointmentDAO.delete(appointmentId);
    }
}