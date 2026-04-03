package com.hms.view;

import com.hms.model.entity.Doctor;
import com.hms.service.DoctorService;
import com.hms.view.ui.HmsTheme;
import com.hms.view.ui.RoundedPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorPanel extends JPanel {

    private final DoctorService doctorService;

    private final JTextField txtName = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Họ tên"}, 0
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
        form.add(new JLabel());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton add = new JButton("Thêm");
        JButton delete = new JButton("Xóa");
        HmsTheme.styleSecondaryButton(add);
        HmsTheme.styleSecondaryButton(delete);

        add.addActionListener(e -> onAdd());
        delete.addActionListener(e -> onDelete());

        actions.add(add);
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
            doctorService.createDoctor(d);
            refreshTable();
            clearForm();
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
            tableModel.addRow(new Object[]{d.getId(), d.getFullName()});
        }
    }

    private void fillFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtName.setText(String.valueOf(tableModel.getValueAt(row, 1)));
    }

    private void clearForm() {
        txtName.setText("");
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

