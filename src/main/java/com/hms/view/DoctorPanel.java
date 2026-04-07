package com.hms.view;

import com.hms.model.entity.Doctor;
import com.hms.service.DoctorService;
import com.hms.view.ui.HmsTheme;
import com.hms.view.ui.RoundedPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.hms.model.entity.Department;

public class DoctorPanel extends JPanel {

    private final DoctorService doctorService;

    private final JTextField txtName = new JTextField();
    private final JComboBox<Department> cbDepartment = new JComboBox<>();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Họ tên", "Khoa"}, 0
    );
    private final JTable table = new JTable(tableModel);

    public DoctorPanel(DoctorService doctorService) {
        this.doctorService = doctorService;

        setOpaque(false);
        setLayout(new BorderLayout(16, 16));

        add(header("Bác sĩ"), BorderLayout.NORTH);
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

        styleField(txtName);

        JPanel form = new JPanel(new GridLayout(1, 2, 10, 10));
        form.setOpaque(false);
        form.add(labeled("Họ tên", txtName));
        
        List<Department> depts = new com.hms.dao.impl.DepartmentDAOImpl().findAll();
        for (Department d : depts) {
            cbDepartment.addItem(d);
        }
        cbDepartment.setSelectedIndex(-1);
        form.add(labeled("Khoa", cbDepartment));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton add = new JButton("Thêm");
        JButton update = new JButton("Sửa");
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

        JLabel h = new JLabel("Danh sách bác sĩ");
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
            Doctor d = new Doctor();
            d.setFullName(txtName.getText());
            if (cbDepartment.getSelectedItem() != null) {
                d.setDepartment((Department) cbDepartment.getSelectedItem());
            }
            doctorService.createDoctor(d);
            refreshTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onUpdate() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 bác sĩ để sửa.");
            return;
        }
        try {
            Long id = Long.parseLong(tableModel.getValueAt(row, 0).toString());
            Doctor d = doctorService.getDoctorById(id);
            d.setFullName(txtName.getText());
            if (cbDepartment.getSelectedItem() != null) {
                d.setDepartment((Department) cbDepartment.getSelectedItem());
            } else {
                d.setDepartment(null);
            }
            doctorService.updateDoctor(d);
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 bác sĩ để xóa.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa bác sĩ đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            Long id = Long.parseLong(tableModel.getValueAt(row, 0).toString());
            doctorService.deleteDoctor(id);
            refreshTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Doctor> list = doctorService.getAllDoctors();
        if (list == null) return;
        for (Doctor d : list) {
            String deptName = "N/A";
            if (d.getDepartment() != null) {
                deptName = d.getDepartment().getName();
            }
            tableModel.addRow(new Object[]{d.getId(), d.getFullName(), deptName});
        }
    }

    private void fillFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtName.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        
        Long doctorId = Long.parseLong(tableModel.getValueAt(row, 0).toString());
        try {
             Doctor d = doctorService.getDoctorById(doctorId);
             if (d.getDepartment() != null) {
                 for (int i = 0; i < cbDepartment.getItemCount(); i++) {
                     if (cbDepartment.getItemAt(i).getId().equals(d.getDepartment().getId())) {
                         cbDepartment.setSelectedIndex(i);
                         break;
                     }
                 }
             } else {
                 cbDepartment.setSelectedIndex(-1);
             }
        } catch (Exception ex) {
             ex.printStackTrace();
        }
    }

    private void clearForm() {
        txtName.setText("");
        cbDepartment.setSelectedIndex(-1);
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

