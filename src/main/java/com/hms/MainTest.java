package com.hms;

import com.hms.dao.impl.AppointmentDAOImpl;
import com.hms.dao.impl.DoctorDAOImpl;
import com.hms.dao.impl.PatientDAOImpl;
import com.hms.model.entity.Appointment;
import com.hms.service.AppointmentService;
import com.hms.service.impl.AppointmentServiceImpl;

import java.time.LocalDateTime;

public class MainTest {
    public static void main(String[] args) {
        // 1. Khởi tạo Service (Truyền các DAO vào)
        AppointmentService appointmentService = new AppointmentServiceImpl(
                new AppointmentDAOImpl(),
                new DoctorDAOImpl(),
                new PatientDAOImpl()
        );

        try {
            // 2. Thử đặt lịch hẹn (Giả sử bạn đã có Patient ID=1 và Doctor ID=1 trong DB)
            // Thay đổi ID cho đúng với dữ liệu mẫu của bạn
            System.out.println("Đang thực hiện đặt lịch...");
            Appointment newApp = appointmentService.bookAppointment(
                    1L,
                    1L,
                    LocalDateTime.now().plusDays(2) // Hẹn vào 2 ngày sau
            );

            System.out.println("=== ĐẶT LỊCH THÀNH CÔNG ===");
            System.out.println("Mã lịch hẹn: " + newApp.getId());
            System.out.println("Bệnh nhân: " + newApp.getPatient().getFullName());
            System.out.println("Bác sĩ: " + newApp.getDoctor().getFullName());
            System.out.println("Thời gian: " + newApp.getAppointmentTime());

        } catch (Exception e) {
            System.err.println("LỖI NGHIỆP VỤ: " + e.getMessage());
        }
    }
}