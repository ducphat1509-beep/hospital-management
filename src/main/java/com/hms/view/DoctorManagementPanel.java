package com.hms.view;

import com.hms.controller.DoctorController;
import com.hms.model.entity.Department;
import com.hms.model.entity.Doctor;
import com.hms.dao.impl.DepartmentDAOImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorManagementPanel extends JPanel {
    private DoctorController doctorController;
    private JTextField txtName;
    private JComboBox<Department> cbDepartment;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnDelete;

    public DoctorManagementPanel(DoctorController controller) {
        this.doctorController = controller;
        setLayout(new BorderLayout());

        // 1. Form nhập liệu
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Bác sĩ"));

        formPanel.add(new JLabel("Họ tên:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Chuyên khoa:"));
        cbDepartment = new JComboBox<>();
        loadDepartments(); // Tải danh sách khoa vào dropdown
        formPanel.add(cbDepartment);

        add(formPanel, BorderLayout.NORTH);

        // 2. Bảng hiển thị
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tên Bác sĩ", "Khoa"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Nút bấm
        JPanel actionPanel = new JPanel();
        btnAdd = new JButton("Thêm Bác sĩ");
        btnDelete = new JButton("Xóa");
        actionPanel.add(btnAdd);
        actionPanel.add(btnDelete);
        add(actionPanel, BorderLayout.SOUTH);

        // Events
        btnAdd.addActionListener(e -> handleAdd());
        btnDelete.addActionListener(e -> handleDelete());

        refreshTable();
    }

    private void loadDepartments() {
        // Khởi tạo trực tiếp để lấy dữ liệu
        com.hms.dao.DepartmentDAO deptDAO = new com.hms.dao.impl.DepartmentDAOImpl();
        List<com.hms.model.entity.Department> list = deptDAO.findAll();
        for (com.hms.model.entity.Department d : list) {
            cbDepartment.addItem(d);
        }
    }

    private void handleAdd() {
        try {
            Doctor d = new Doctor();
            d.setFullName(txtName.getText());
            d.setDepartment((Department) cbDepartment.getSelectedItem());

            doctorController.addDoctor(d);
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Long id = (Long) table.getValueAt(row, 0);
            doctorController.deleteDoctor(id);
            refreshTable();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Doctor> doctors = doctorController.getAllDoctors();
        if (doctors != null) {
            for (Doctor d : doctors) {
                String deptName = "N/A";
                // Kiểm tra xem bác sĩ có khoa không và khoa đã được nạp dữ liệu chưa
                if (d.getDepartment() != null) {
                    try {
                        deptName = d.getDepartment().getName();
                    } catch (Exception e) {
                        deptName = "Lỗi nạp dữ liệu";
                    }
                }

                tableModel.addRow(new Object[]{
                        d.getId(),
                        d.getFullName(),
                        deptName
                });
            }
        }
    }
}