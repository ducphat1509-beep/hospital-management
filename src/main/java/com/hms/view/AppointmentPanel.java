package com.hms.view;

import com.hms.controller.AppointmentController;
import com.hms.model.entity.Appointment;
import com.hms.model.entity.Doctor;
import com.hms.model.entity.Patient;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentPanel extends JPanel {
    private AppointmentController controller;

    // Components cho phần đặt lịch
    private JComboBox<Patient> cbPatient;
    private JComboBox<Doctor> cbDoctor;

    // Components cho phần danh sách và lọc
    private JComboBox<Doctor> cbFilterDoctor;
    private JTable table;
    private DefaultTableModel tableModel;

    public AppointmentPanel(AppointmentController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // --- 1. Top Panel: Đặt lịch và Lọc ---
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        // Dòng 1: Form đặt lịch
        JPanel bookingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bookingPanel.setBorder(BorderFactory.createTitledBorder("Đặt lịch mới"));
        cbPatient = new JComboBox<>();
        cbDoctor = new JComboBox<>();
        JButton btnBook = new JButton("Đặt lịch");

        bookingPanel.add(new JLabel("Bệnh nhân:"));
        bookingPanel.add(cbPatient);
        bookingPanel.add(new JLabel("Bác sĩ:"));
        bookingPanel.add(cbDoctor);
        bookingPanel.add(btnBook);

        // Dòng 2: Bộ lọc bác sĩ
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc danh sách"));
        cbFilterDoctor = new JComboBox<>();
        JButton btnRefresh = new JButton("Làm mới/Tất cả");

        filterPanel.add(new JLabel("Xem lịch theo bác sĩ:"));
        filterPanel.add(cbFilterDoctor);
        filterPanel.add(btnRefresh);

        topPanel.add(bookingPanel);
        topPanel.add(filterPanel);
        add(topPanel, BorderLayout.NORTH);

        // --- 2. Center Panel: Bảng danh sách ---
        String[] columns = {"ID", "Bệnh nhân", "Bác sĩ", "Thời gian", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

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

    private void loadInitialData() {
        List<Patient> patients = controller.getPatients();
        List<Doctor> doctors = controller.getDoctors();

        patients.forEach(cbPatient::addItem);
        doctors.forEach(d -> {
            cbDoctor.addItem(d);
            cbFilterDoctor.addItem(d); // Thêm vào cả ô lọc
        });
        cbFilterDoctor.setSelectedIndex(-1); // Mặc định không lọc
    }

    private void handleBook() {
        Patient p = (Patient) cbPatient.getSelectedItem();
        Doctor d = (Doctor) cbDoctor.getSelectedItem();
        if (p != null && d != null) {
            try {
                controller.createAppointment(p.getId(), d.getId(), LocalDateTime.now().plusDays(1));
                JOptionPane.showMessageDialog(this, "Đặt lịch thành công!");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        Doctor selectedDoctor = (Doctor) cbFilterDoctor.getSelectedItem();
        Long filterId = (selectedDoctor != null) ? selectedDoctor.getId() : null;

        List<Appointment> list = (filterId == null)
                ? controller.getAllAppointments()
                : controller.getAppointmentsByDoctor(filterId);

        for (Appointment a : list) {
            tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getPatient().getFullName(),
                    a.getDoctor().getFullName(),
                    a.getAppointmentTime(),
                    a.getStatus()
            });
        }
    }
}