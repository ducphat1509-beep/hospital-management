package com.hms.view;

import com.hms.model.entity.Patient;
import com.hms.service.PatientService;
import com.hms.view.ui.HmsTheme;
import com.hms.view.ui.RoundedPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PatientPanel extends JPanel {

    private final PatientService patientService;

    private final JTextField txtName = new JTextField();
    private final JTextField txtPhone = new JTextField();
    private final JTextField txtDob = new JTextField();
    private final JComboBox<String> cbGender = new JComboBox<>(new String[]{"MALE", "FEMALE", "OTHER"});

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Họ Tên", "SĐT", "Ngày Sinh", "Giới tính"}, 0
    );
    private final JTable table = new JTable(tableModel);

    public PatientPanel(PatientService patientService) {
        this.patientService = patientService;

        setOpaque(false);
        setLayout(new BorderLayout(16, 16));

        add(header("Bệnh nhân"), BorderLayout.NORTH);
        add(body(), BorderLayout.CENTER);

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

    private JComponent body() {
        JPanel wrap = new JPanel(new BorderLayout(16, 16));
        wrap.setOpaque(false);

        wrap.add(formCard(), BorderLayout.NORTH);
        wrap.add(tableCard(), BorderLayout.CENTER);
        return wrap;
    }

    private JComponent formCard() {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setOpaque(false);

        styleField(txtName);
        styleField(txtPhone);
        styleField(txtDob);
        cbGender.setSelectedIndex(-1);
        cbGender.setFont(HmsTheme.fontRegular(12));

        form.add(labeled("Họ tên", txtName));
        form.add(labeled("Số điện thoại", txtPhone));
        form.add(labeled("Ngày sinh (yyyy-mm-dd)", txtDob));
        form.add(labeled("Giới tính", cbGender));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton add = new JButton("Thêm");
        JButton update = new JButton("Cập nhật");
        JButton delete = new JButton("Xóa");
        HmsTheme.styleSecondaryButton(add);
        HmsTheme.styleSecondaryButton(update);
        HmsTheme.styleSecondaryButton(delete);

        add.addActionListener(e -> onAdd());
        update.addActionListener(e -> onUpdate());
        delete.addActionListener(e -> onDelete());

        actions.add(add);
        actions.add(update);
        actions.add(delete);

        card.add(form, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private JComponent tableCard() {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel h = new JLabel("Danh sách bệnh nhân");
        h.setFont(HmsTheme.fontBold(14));
        h.setForeground(HmsTheme.TEXT);

        HmsTheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> fillFromSelection());

        card.add(h, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private void onAdd() {
        try {
            Patient p = new Patient();
            p.setFullName(txtName.getText());
            p.setPhone(txtPhone.getText());

            String dob = txtDob.getText() != null ? txtDob.getText().trim() : "";
            p.setDob(dob.isEmpty() ? null : LocalDate.parse(dob));

            Object gender = cbGender.getSelectedItem();
            p.setGender(gender == null ? null : Patient.Gender.valueOf(gender.toString()));

            patientService.registerPatient(p);
            refreshTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onUpdate() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 bệnh nhân để cập nhật.");
            return;
        }
        try {
            Long id = Long.parseLong(tableModel.getValueAt(row, 0).toString());
            Patient p = new Patient();
            p.setId(id);
            p.setFullName(txtName.getText());
            p.setPhone(txtPhone.getText());

            String dob = txtDob.getText() != null ? txtDob.getText().trim() : "";
            p.setDob(dob.isEmpty() ? null : LocalDate.parse(dob));

            Object gender = cbGender.getSelectedItem();
            p.setGender(gender == null ? null : Patient.Gender.valueOf(gender.toString()));

            patientService.updatePatientInfo(p);
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 bệnh nhân để xóa.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa bệnh nhân đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            Long id = Long.parseLong(tableModel.getValueAt(row, 0).toString());
            patientService.removePatient(id);
            refreshTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Patient> list = patientService.getAllPatients();
        if (list == null) return;
        for (Patient p : list) {
            tableModel.addRow(new Object[]{p.getId(), p.getFullName(), p.getPhone(), p.getDob(), p.getGender()});
        }
    }

    private void fillFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtName.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtPhone.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        Object dob = tableModel.getValueAt(row, 3);
        txtDob.setText(dob == null ? "" : dob.toString());
        Object gender = tableModel.getValueAt(row, 4);
        cbGender.setSelectedItem(gender == null ? null : gender.toString());
    }

    private void clearForm() {
        txtName.setText("");
        txtPhone.setText("");
        txtDob.setText("");
        cbGender.setSelectedIndex(-1);
        table.clearSelection();
    }

    private static JComponent labeled(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(HmsTheme.fontRegular(12));
        l.setForeground(HmsTheme.TEXT_MUTED);
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private static void styleField(JTextField field) {
        field.setFont(HmsTheme.fontRegular(12));
        field.setBorder(HmsTheme.roundedLineBorder(16));
        field.setBackground(Color.WHITE);
        field.setForeground(HmsTheme.TEXT);
    }
}

