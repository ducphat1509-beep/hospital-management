package com.hms.view;

// 1. IMPORT các lớp từ các package khác
import com.hms.controller.AppointmentController;
import com.hms.controller.DoctorController;
import com.hms.controller.PatientController;
import com.hms.dao.AppointmentDAO;
import com.hms.dao.DoctorDAO;
import com.hms.dao.PatientDAO;
import com.hms.dao.impl.AppointmentDAOImpl;
import com.hms.dao.impl.DoctorDAOImpl;
import com.hms.dao.impl.PatientDAOImpl;
import com.hms.service.AppointmentService;
import com.hms.service.DoctorService;
import com.hms.service.PatientService;
import com.hms.service.impl.AppointmentServiceImpl;
import com.hms.service.impl.DoctorServiceImpl;
import com.hms.service.impl.PatientServiceImpl;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        // --- PHẦN 1: CẤU HÌNH CỬA SỔ (UI) ---
        setTitle("HỆ THỐNG QUẢN LÝ BỆNH VIỆN - HMS");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- PHẦN 2: KHỞI TẠO CÁC TẦNG (BACKEND-TO-FRONTEND) ---
        // Khởi tạo từ tầng thấp nhất (DAO) lên tầng cao nhất (Controller)
        // 1. Tầng Patient
        PatientDAO patientDAO = new PatientDAOImpl();
        PatientService patientService = new PatientServiceImpl(patientDAO);
        PatientController patientController = new PatientController(patientService);

        // 2. Tầng Doctor
        DoctorDAO doctorDAO = new DoctorDAOImpl();
        DoctorService doctorService = new DoctorServiceImpl(doctorDAO);
        DoctorController doctorController = new DoctorController(doctorService);

        // 3. Tầng Appointment (Cần cả 3 DAO để hoạt động)
        AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
        AppointmentService appointmentService = new AppointmentServiceImpl(appointmentDAO, doctorDAO, patientDAO);
        AppointmentController appointmentController = new AppointmentController(appointmentService, doctorService, patientService);

        // --- PHẦN 3: TẠO CÁC MẢNH GHÉP GIAO DIỆN (PANELS) ---
        // Nạp Controller vào View tương ứng
        PatientManagementPanel patientPanel = new PatientManagementPanel(patientController);

        DoctorManagementPanel doctorPanel = new DoctorManagementPanel(doctorController);

        AppointmentPanel appointmentPanel = new AppointmentPanel(appointmentController);

        // --- PHẦN 4: LẮP RÁP VÀO TABBED PANE ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Quản lý Bệnh nhân", patientPanel);
        tabbedPane.addTab("Quản lý Bác sĩ", doctorPanel);
        tabbedPane.addTab("Đặt lịch hẹn", appointmentPanel);

        // Thêm TabbedPane vào khung chính
        this.add(tabbedPane);
    }

    public static void main(String[] args) {
        // Thiết lập giao diện theo hệ điều hành cho đẹp
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chạy ứng dụng
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}