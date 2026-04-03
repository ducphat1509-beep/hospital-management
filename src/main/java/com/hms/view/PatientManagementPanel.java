package com.hms.view;

import com.hms.controller.PatientController;
import com.hms.model.entity.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.time.LocalDate;
import java.util.List;

public class PatientManagementPanel extends JPanel {

    // Thêm biến controller vào đầu Class
    private PatientController patientController;

    // Các thành phần giao diện
    private JTextField txtName, txtPhone, txtDob;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete;
    private JComboBox<String> cbGender;

    public PatientManagementPanel(PatientController controller) {
        this.patientController = controller;

        // Thiết lập bố cục tổng thể là BorderLayout (Bắc-Trung-Nam)
        setLayout(new BorderLayout());

        // 1. Phần Form nhập liệu (Phía Bắc - NORTH)
        // Dùng GridLayout(3 hàng, 2 cột) để Label và TextField thẳng hàng
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin bệnh nhân"));

        formPanel.add(new JLabel("Họ tên:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Ngày sinh (yyyy-mm-dd):"));
        txtDob = new JTextField();
        formPanel.add(txtDob);

        formPanel.add(new JLabel("Giới tính:"));
        cbGender = new JComboBox<>(new String[]{"MALE", "FEMALE", "OTHER"});
        formPanel.add(cbGender);
        cbGender.setSelectedIndex(-1);

        add(formPanel, BorderLayout.NORTH);

        // 2. Phần Bảng hiển thị (Phía Trung tâm - CENTER)
        tableModel = new DefaultTableModel(new Object[]{"ID", "Họ Tên", "SĐT", "Ngày Sinh", "gender"}, 0);
        table = new JTable(tableModel);
        // JScrollPane giúp bảng có thanh cuộn khi dữ liệu nhiều
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Phần Nút bấm (Phía Nam - SOUTH)
        JPanel actionPanel = new JPanel();
        btnAdd = new JButton("Thêm mới");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        actionPanel.add(btnAdd);
        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);
        add(actionPanel, BorderLayout.SOUTH);

        // Gắn sự kiện (Dây điện) cho nút Thêm
        btnAdd.addActionListener(e -> handleAddPatient());
        // Gắn sự kiện cho các nút còn lại (Update, Delete)
        btnUpdate.addActionListener(e -> handleUpdatePatient());
        btnDelete.addActionListener(e -> handleDeletePatient());

        // Sự kiện khi click vào một dòng trong bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Lấy dữ liệu từ bảng đổ ngược lại các ô nhập liệu
                txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtPhone.setText(tableModel.getValueAt(selectedRow, 2).toString());
                // Lưu ý: Kiểm tra null cho ngày sinh nếu cần
                Object dob = tableModel.getValueAt(selectedRow, 3);
                txtDob.setText(dob != null ? dob.toString() : "");
            }
        });

        // Load dữ liệu từ DB lên bảng ngay khi vừa mở app
        refreshTable();
    }

    private void handleAddPatient() {
        try {
            // Lấy dữ liệu từ các ô nhập liệu vào Object
            Patient p = new Patient();
            p.setFullName(txtName.getText());
            p.setPhone(txtPhone.getText());
            p.setGender(Patient.Gender.valueOf(cbGender.getSelectedItem().toString()));
            // XỬ LÝ NGÀY SINH Ở ĐÂY
            String dobString = txtDob.getText().trim();
            if (!dobString.isEmpty()) {
                try {
                    p.setFullName(txtName.getText());
                    p.setPhone(txtPhone.getText());
                    p.setGender(Patient.Gender.valueOf(cbGender.getSelectedItem().toString())); // Thêm Gender

                    // Xử lý Ngày sinh: Chuyển String yyyy-MM-dd sang LocalDate
                    String dobStr = txtDob.getText();
                    if (!dobStr.isEmpty()) {
                        p.setDob(java.time.LocalDate.parse(dobStr));
                    }

                    patientController.addPatient(p);
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    refreshTable();
                } catch (java.time.format.DateTimeParseException e) {
                    JOptionPane.showMessageDialog(this, "Ngày sinh sai định dạng! (Dùng yyyy-MM-dd)");
                }
            } else {
                p.setDob(null);
            }

            // Gọi Controller để xử lý nghiệp vụ lưu xuống DB
            patientController.addPatient(p);

            // Thông báo thành công
            JOptionPane.showMessageDialog(this, "Thêm bệnh nhân thành công!");

            // Cập nhật lại bảng hiển thị
            refreshTable();
            // Xóa sạch chữ trong ô nhập để người dùng nhập tiếp
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void handleUpdatePatient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bệnh nhân trong bảng để cập nhật!");
            return;
        }

        try {
            // 1. Lấy ID từ cột đầu tiên của dòng đang chọn
            // Ép kiểu qua String trước rồi parse() để tránh lỗi ClassCastException khi click trên Table
            Long id = Long.parseLong(tableModel.getValueAt(selectedRow, 0).toString());

            // 2. Tạo đối tượng Patient với dữ liệu mới
            Patient p = new Patient();
            p.setId(id);
            p.setFullName(txtName.getText());
            p.setPhone(txtPhone.getText());
            // XỬ LÝ NGÀY SINH Ở ĐÂY
            String dobString = txtDob.getText().trim();
            if (!dobString.isEmpty()) {
                try {
                    // Mặc định LocalDate.parse hiểu định dạng yyyy-MM-dd (vd: 1999-12-31)
                    LocalDate date = LocalDate.parse(dobString);
                    p.setDob(date);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Ngày sinh sai định dạng! Vui lòng nhập: yyyy-MM-dd");
                    return; // Dừng hàm, không lưu nữa
                }
            } else {
                p.setDob(null);
            }

            // 3. Gọi Controller xử lý
            patientController.updatePatient(p);

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            refreshTable();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
        }
    }

    private void handleDeletePatient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bệnh nhân cần xóa!");
            return;
        }

        // Hỏi lại cho chắc chắn
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa bệnh nhân này?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Long id = Long.parseLong(tableModel.getValueAt(selectedRow, 0).toString());
                patientController.deletePatient(id); // Gọi xuống Controller

                JOptionPane.showMessageDialog(this, "Đã xóa bệnh nhân!");
                refreshTable();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage());
            }
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtPhone.setText("");
        txtDob.setText("");
        table.clearSelection();
    }

    // Hàm này dùng để lấy dữ liệu mới nhất từ DB và vẽ lại bảng
    private void refreshTable() {
        tableModel.setRowCount(0); // Xóa hết các dòng cũ trên giao diện
        List<Patient> list = patientController.getAllPatients();
        for (Patient p : list) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getFullName(),
                    p.getPhone(),
                    p.getDob(),
                    p.getGender()
            });
        }
    }

}