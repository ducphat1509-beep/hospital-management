package com.hms.view;

import com.hms.dao.AppointmentDAO;
import com.hms.dao.BillDAO;
import com.hms.dao.BillMedicineDetailDAO;
import com.hms.dao.DoctorDAO;
import com.hms.dao.MedicalRecordDAO;
import com.hms.dao.MedicineDAO;
import com.hms.dao.PatientDAO;
import com.hms.dao.PrescriptionDAO;
import com.hms.dao.impl.AppointmentDAOImpl;
import com.hms.dao.impl.BillDAOImpl;
import com.hms.dao.impl.BillMedicineDetailDAOImpl;
import com.hms.dao.impl.DoctorDAOImpl;
import com.hms.dao.impl.MedicalRecordDAOImpl;
import com.hms.dao.impl.MedicineDAOImpl;
import com.hms.dao.impl.PatientDAOImpl;
import com.hms.dao.impl.PrescriptionDAOImpl;
import com.hms.model.dto.UserSessionDTO;
import com.hms.model.entity.Account;
import com.hms.model.enumtype.UserRole;
import com.hms.service.AppointmentService;
import com.hms.service.BillMedicineDetailService;
import com.hms.service.BillService;
import com.hms.service.DoctorService;
import com.hms.service.MedicalRecordService;
import com.hms.service.MedicineService;
import com.hms.service.PatientService;
import com.hms.service.PrescriptionService;
import com.hms.service.impl.AppointmentServiceImpl;
import com.hms.service.impl.BillMedicineDetailServiceImpl;
import com.hms.service.impl.BillServiceImpl;
import com.hms.service.impl.DoctorServiceImpl;
import com.hms.service.impl.MedicalRecordServiceImpl;
import com.hms.service.impl.MedicineServiceImpl;
import com.hms.service.impl.PatientServiceImpl;
import com.hms.service.impl.PrescriptionServiceImpl;
import com.hms.view.ui.HmsTheme;
import com.hms.view.ui.RoundedPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private UserSessionDTO currentUser; // Lưu tài khoản hiện tại

    public MainFrame(UserSessionDTO session) {
        this.currentUser = session;
        // Gọi hàm phân quyền sau khi các UI component đã được khởi tạo


        setTitle("HỆ THỐNG QUẢN LÝ BỆNH VIỆN - HMS");
        setSize(1200, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            java.net.URL iconUrl = getClass().getResource("/assets/logoHospital.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception ignored) {
        }

        // --- Backend wiring (DAO -> Service) ---
        PatientDAO patientDAO = new PatientDAOImpl();
        PatientService patientService = new PatientServiceImpl(patientDAO);

        DoctorDAO doctorDAO = new DoctorDAOImpl();
        DoctorService doctorService = new DoctorServiceImpl(doctorDAO);

        AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
        AppointmentService appointmentService = new AppointmentServiceImpl(appointmentDAO, doctorDAO, patientDAO);

        MedicineDAO medicineDAO = new MedicineDAOImpl();
        MedicineService medicineService = new MedicineServiceImpl(medicineDAO);

        MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAOImpl();
        MedicalRecordService medicalRecordService = new MedicalRecordServiceImpl(medicalRecordDAO, appointmentDAO);

        PrescriptionDAO prescriptionDAO = new PrescriptionDAOImpl();
        PrescriptionService prescriptionService = new PrescriptionServiceImpl(prescriptionDAO, medicalRecordDAO);

        com.hms.dao.PrescriptionDetailDAO prescriptionDetailDAO = new com.hms.dao.impl.PrescriptionDetailDAOImpl();
        com.hms.service.PrescriptionDetailService prescriptionDetailService = new com.hms.service.impl.PrescriptionDetailServiceImpl(prescriptionDetailDAO, prescriptionDAO, medicineDAO);

        BillDAO billDAO = new BillDAOImpl();
        BillMedicineDetailDAO billMedicineDetailDAO = new BillMedicineDetailDAOImpl();
        BillService billService = new BillServiceImpl(billDAO, billMedicineDetailDAO, patientDAO);
        BillMedicineDetailService billMedicineDetailService = new BillMedicineDetailServiceImpl(
                billDAO,
                billMedicineDetailDAO,
                medicineDAO,
                medicineService,
                billService
        );

        // --- UI shell: BorderLayout + sidebar + topbar + CardLayout content ---
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(HmsTheme.BG);
        setContentPane(root);

        JPanel sidebar = buildSidebar();
        JPanel topbar = buildTopbar();

        CardLayout cardLayout = new CardLayout();
        JPanel content = new JPanel(cardLayout);
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        DashboardPanel dashboardPanel = new DashboardPanel(appointmentService, () -> cardLayout.show(content, Nav.APPOINTMENTS));
        PatientPanel patientPanel = new PatientPanel(patientService);
        DoctorPanel doctorPanel = new DoctorPanel(doctorService);
        AppointmentPanel appointmentPanel = new AppointmentPanel(appointmentService, patientService, doctorService, currentUser);
        MedicinePanel medicinePanel = new MedicinePanel(medicineService);
        PrescriptionPanel prescriptionPanel = new PrescriptionPanel(appointmentService, medicalRecordService, prescriptionService, medicineService, prescriptionDetailService, billService, billMedicineDetailService);
        BillPanel billPanel = new BillPanel(billService, billMedicineDetailService, medicineService, patientService, currentUser);

        content.add(dashboardPanel, Nav.DASHBOARD);
        content.add(patientPanel, Nav.PATIENTS);
        content.add(doctorPanel, Nav.DOCTORS);
        content.add(appointmentPanel, Nav.APPOINTMENTS);
        content.add(medicinePanel, Nav.MEDICINES);
        content.add(prescriptionPanel, Nav.PRESCRIPTIONS);
        content.add(billPanel, Nav.BILLS);

        JPanel contentWrap = new RoundedPanel(18, HmsTheme.CARD);
        contentWrap.setLayout(new BorderLayout());
        contentWrap.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        contentWrap.add(content, BorderLayout.CENTER);

        root.add(sidebar, BorderLayout.WEST);
        root.add(topbar, BorderLayout.NORTH);
        root.add(contentWrap, BorderLayout.CENTER);

        wireNavigation(sidebar, cardLayout, content);
        cardLayout.show(content, Nav.DASHBOARD);
    }

    private static final class Nav {
        static final String DASHBOARD = "dashboard";
        static final String PATIENTS = "patients";
        static final String DOCTORS = "doctors";
        static final String APPOINTMENTS = "appointments";
        static final String MEDICINES = "medicines";
        static final String PRESCRIPTIONS = "prescriptions";
        static final String BILLS = "bills";
    }

    private JPanel buildSidebar() {
        RoundedPanel panel = new RoundedPanel(18, HmsTheme.SIDEBAR_BG);
        panel.setPreferredSize(new Dimension(240, 10));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel brand = new JLabel("HMS");
        brand.setFont(HmsTheme.fontBold(18));
        brand.setForeground(HmsTheme.TEXT);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(brand, BorderLayout.WEST);
        panel.add(top, BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setOpaque(false);
        nav.setLayout(new GridLayout(0, 1, 0, 8));

        // Những quyền cơ bản ai cũng có
        nav.add(HmsTheme.navButton("Trang chủ", Nav.DASHBOARD));
        nav.add(HmsTheme.navButton("Lịch hẹn", Nav.APPOINTMENTS));

        // PHÂN QUYỀN TẠI ĐÂY
        UserRole role = currentUser.getRole();

        if (role == UserRole.ADMIN || role == UserRole.RECEPTIONIST) {
            nav.add(HmsTheme.navButton("Bệnh nhân", Nav.PATIENTS));
            nav.add(HmsTheme.navButton("Hóa đơn", Nav.BILLS));
        }

        if (role == UserRole.ADMIN || role == UserRole.DOCTOR) {
            nav.add(HmsTheme.navButton("Đơn thuốc", Nav.PRESCRIPTIONS));
        }

        if (role == UserRole.ADMIN) {
            nav.add(HmsTheme.navButton("Bác sĩ", Nav.DOCTORS));
            nav.add(HmsTheme.navButton("Thuốc", Nav.MEDICINES));
        }

        panel.add(nav, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildTopbar() {
        RoundedPanel panel = new RoundedPanel(18, HmsTheme.TOPBAR_BG);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel left = new JPanel(new GridLayout(0, 1, 0, 2));
        left.setOpaque(false);

        String name = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : currentUser.getUsername();
        JLabel hello = new JLabel("Chào mừng bạn trở lại " + name);
        hello.setFont(HmsTheme.fontBold(18));
        hello.setForeground(HmsTheme.TEXT);
        JLabel sub = new JLabel("Hospital Management System");
        sub.setFont(HmsTheme.fontRegular(12));
        sub.setForeground(HmsTheme.TEXT_MUTED);
        left.add(hello);
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
//        JTextField search = new JTextField(22);
//        search.setFont(HmsTheme.fontRegular(12));
//        search.setBorder(HmsTheme.roundedLineBorder(16));
//        search.setBackground(Color.WHITE);
//        search.setForeground(HmsTheme.TEXT);
//        search.setToolTipText("Search");
//        right.add(search);

        JButton userBtn = new JButton(currentUser.getRole().toString());
        HmsTheme.stylePillButton(userBtn);
        right.add(userBtn);

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private void wireNavigation(JPanel sidebar, CardLayout cardLayout, JPanel content) {
        attachNavListeners(sidebar, cardLayout, content);
    }

    private void attachNavListeners(Container root, CardLayout cardLayout, JPanel content) {
        for (Component c : root.getComponents()) {
            if (c instanceof JButton b) {
                Object key = b.getClientProperty("navKey");
                if (key instanceof String navKey) {
                    b.addActionListener(e -> cardLayout.show(content, navKey));
                }
            }
            if (c instanceof Container child) {
                attachNavListeners(child, cardLayout, content);
            }
        }
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
            // Khởi tạo các thành phần cần thiết
            com.hms.dao.AccountDAO accountDAO = new com.hms.dao.impl.AccountDAOImpl();
            com.hms.service.AuthService authService = new com.hms.service.impl.AuthServiceImpl(accountDAO);

            // Hiện màn hình login trước
            new LoginFrame(authService).setVisible(true);
        });
    }
}