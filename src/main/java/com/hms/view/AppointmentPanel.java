package com.hms.view;

import com.hms.model.dto.UserSessionDTO;
import com.hms.model.entity.Appointment;
import com.hms.model.entity.Doctor;
import com.hms.model.entity.Patient;
import com.hms.service.AppointmentService;
import com.hms.service.DoctorService;
import com.hms.service.PatientService;
import com.hms.view.ui.HmsTheme;
import com.hms.view.ui.RoundedPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import com.hms.model.entity.Account;
import com.hms.model.enumtype.UserRole;

public class AppointmentPanel extends JPanel {
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final UserSessionDTO session;

    // Components cho phần đặt lịch
    private JComboBox<Patient> cbPatient;
    private JComboBox<Doctor> cbDoctor;
    private JTextField txtTime;

    // Components cho phần danh sách và lọc
    private JComboBox<Doctor> cbFilterDoctor;
    private JTable table;
    private DefaultTableModel tableModel;

    public AppointmentPanel(AppointmentService appointmentService, PatientService patientService, DoctorService doctorService, UserSessionDTO session) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.session = session;

        setOpaque(false);
        setLayout(new BorderLayout(16, 16));

        add(header("Lịch hẹn"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 16));
        body.setOpaque(false);

        // --- Booking + Filter cards ---
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 12));
        topPanel.setOpaque(false);

        RoundedPanel bookingCard = new RoundedPanel(18, Color.WHITE);
        bookingCard.setLayout(new BorderLayout(12, 12));
        bookingCard.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel bookingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bookingPanel.setOpaque(false);
        cbPatient = new JComboBox<>();
        cbDoctor = new JComboBox<>();
        txtTime = new JTextField(18);
        txtTime.setFont(HmsTheme.fontRegular(12));
        txtTime.setBorder(HmsTheme.roundedLineBorder(16));
        txtTime.setToolTipText("yyyy-MM-dd HH:mm:ss (vd: 2026-04-02 14:30:16)");
        JButton btnBook = new JButton("Đặt lịch");
        HmsTheme.styleSecondaryButton(btnBook);

        bookingPanel.add(new JLabel("Bệnh nhân:"));
        bookingPanel.add(cbPatient);
        bookingPanel.add(new JLabel("Bác sĩ:"));
        bookingPanel.add(cbDoctor);
        bookingPanel.add(new JLabel("Thời gian:"));
        bookingPanel.add(txtTime);
        bookingPanel.add(btnBook);

        JLabel bookingTitle = new JLabel("Đặt lịch mới");
        bookingTitle.setFont(HmsTheme.fontBold(14));
        bookingTitle.setForeground(HmsTheme.TEXT);
        bookingCard.add(bookingTitle, BorderLayout.NORTH);
        bookingCard.add(bookingPanel, BorderLayout.CENTER);

        // --- Filter card ---
        RoundedPanel filterCard = new RoundedPanel(18, Color.WHITE);
        filterCard.setLayout(new BorderLayout(12, 12));
        filterCard.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);
        cbFilterDoctor = new JComboBox<>();
        JButton btnRefresh = new JButton("Làm mới/Tất cả");
        HmsTheme.styleSecondaryButton(btnRefresh);

        filterPanel.add(new JLabel("Xem lịch theo bác sĩ:"));
        filterPanel.add(cbFilterDoctor);
        filterPanel.add(btnRefresh);

        JLabel filterTitle = new JLabel("Bộ lọc danh sách");
        filterTitle.setFont(HmsTheme.fontBold(14));
        filterTitle.setForeground(HmsTheme.TEXT);
        filterCard.add(filterTitle, BorderLayout.NORTH);
        filterCard.add(filterPanel, BorderLayout.CENTER);

        topPanel.add(bookingCard);
        topPanel.add(filterCard);
        body.add(topPanel, BorderLayout.NORTH);

        // --- Table card ---
        String[] columns = {"ID", "Bệnh nhân", "Bác sĩ", "Thời gian", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        HmsTheme.styleTable(table);

        RoundedPanel tableCard = new RoundedPanel(18, Color.WHITE);
        tableCard.setLayout(new BorderLayout(12, 12));
        tableCard.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel listTitle = new JLabel("Danh sách lịch hẹn");
        listTitle.setFont(HmsTheme.fontBold(14));
        listTitle.setForeground(HmsTheme.TEXT);
        tableCard.add(listTitle, BorderLayout.NORTH);
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);
        if (session.getRole() == UserRole.RECEPTIONIST || session.getRole() == UserRole.ADMIN) {
            JButton btnDone = new JButton("Hoàn thành (DONE)");
            JButton btnConfirmed = new JButton("Xác nhận (CONFIRMED)");
            JButton btnCancel = new JButton("Hủy (CANCELLED)");
            HmsTheme.styleSecondaryButton(btnDone);
            HmsTheme.styleSecondaryButton(btnConfirmed);
            HmsTheme.styleSecondaryButton(btnCancel);
            
            btnDone.addActionListener(e -> updateStatus(Appointment.AppointmentStatus.DONE));
            btnConfirmed.addActionListener(e -> updateStatus(Appointment.AppointmentStatus.CONFIRMED));
            btnCancel.addActionListener(e -> updateStatus(Appointment.AppointmentStatus.CANCELLED));
            
            actionPanel.add(btnDone);
            actionPanel.add(btnConfirmed);
            actionPanel.add(btnCancel);
            tableCard.add(actionPanel, BorderLayout.SOUTH);
        } else if (session.getRole() == UserRole.DOCTOR) {
            JButton btnDone = new JButton("Hoàn thành (DONE)");
            JButton btnConfirmed = new JButton("Xác nhận (CONFIRMED)");
            HmsTheme.styleSecondaryButton(btnDone);
            HmsTheme.styleSecondaryButton(btnConfirmed);

            btnDone.addActionListener(e -> updateStatus(Appointment.AppointmentStatus.DONE));
            btnConfirmed.addActionListener(e -> updateStatus(Appointment.AppointmentStatus.CONFIRMED));

            actionPanel.add(btnDone);
            actionPanel.add(btnConfirmed);
            tableCard.add(actionPanel, BorderLayout.SOUTH);
        }
        
        body.add(tableCard, BorderLayout.CENTER);

        add(body, BorderLayout.CENTER);

        // --- 3. Events ---
        btnBook.addActionListener(e -> handleBook());

        // Sự kiện khi thay đổi giá trị trong ô Lọc
        cbFilterDoctor.addActionListener(e -> refreshTable());

        btnRefresh.addActionListener(e -> {
            cbFilterDoctor.setSelectedIndex(-1); // Bỏ chọn lọc
            refreshTable();
        });

        // Load dữ liệu ban đầu
        loadInitialData();
        refreshTable();
    }

    private JComponent header(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(HmsTheme.fontBold(18));
        t.setForeground(HmsTheme.TEXT);
        p.add(t, BorderLayout.WEST);
        return p;
    }

    private void loadInitialData() {
        List<Patient> patients = patientService.getAllPatients();
        List<Doctor> doctors = doctorService.getAllDoctors();

        patients.forEach(cbPatient::addItem);
        doctors.forEach(d -> {
            cbDoctor.addItem(d);
            cbFilterDoctor.addItem(d); // Thêm vào cả ô lọc
        });
        cbFilterDoctor.setSelectedIndex(-1); // Mặc định không lọc
        cbDoctor.setSelectedIndex(-1);
        cbPatient.setSelectedIndex(-1);
    }

    private void handleBook() {
        Patient p = (Patient) cbPatient.getSelectedItem();
        Doctor d = (Doctor) cbDoctor.getSelectedItem();
        if (p != null && d != null) {
            try {
                LocalDateTime time;
                String raw = txtTime.getText() != null ? txtTime.getText().trim() : "";
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                if (raw.isEmpty()) {
                    time = LocalDateTime.now().plusDays(1);
                } else {
                    time = LocalDateTime.parse(raw, formatter);
                }
                appointmentService.bookAppointment(p.getId(), d.getId(), time);
                JOptionPane.showMessageDialog(this, "Đặt lịch thành công!");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void updateStatus(Appointment.AppointmentStatus status) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 lịch hẹn để cập nhật!");
            return;
        }
        try {
            Long currentId = Long.parseLong(tableModel.getValueAt(row, 0).toString());
            // Chỉ cần cập nhật nếu thực sự chọn Update
            appointmentService.updateStatus(currentId, status);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        Doctor selectedDoctor = (Doctor) cbFilterDoctor.getSelectedItem();
        Long filterId = (selectedDoctor != null) ? selectedDoctor.getId() : null;

        List<Appointment> list = (filterId == null)
                ? appointmentService.getAppointmentsByDoctor(null)
                : appointmentService.getAppointmentsByDoctor(filterId);

        for (Appointment a : list) {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getPatient().getFullName(),
                    a.getDoctor().getFullName(),
                    a.getAppointmentTime() != null ? a.getAppointmentTime().format(formatter) : "",
                    a.getStatus()
            });
        }
    }
}