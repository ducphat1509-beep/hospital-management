package com.hms.view;

import com.hms.model.entity.MedicalRecord;
import com.hms.model.entity.Prescription;
import com.hms.service.MedicalRecordService;
import com.hms.service.PrescriptionService;
import com.hms.view.ui.HmsTheme;
import com.hms.view.ui.RoundedPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PrescriptionPanel extends JPanel {

    private final PrescriptionService prescriptionService;
    private final MedicalRecordService medicalRecordService;

    private final JTextField txtRecordId = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Record ID", "Created At"}, 0
    );
    private final JTable table = new JTable(tableModel);

    public PrescriptionPanel(PrescriptionService prescriptionService, MedicalRecordService medicalRecordService) {
        this.prescriptionService = prescriptionService;
        this.medicalRecordService = medicalRecordService;

        setOpaque(false);
        setLayout(new BorderLayout(16, 16));

        add(header("Đơn thuốc"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 16));
        body.setOpaque(false);
        body.add(formCard(), BorderLayout.NORTH);
        body.add(tableCard(), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

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

    private JComponent formCard() {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        styleField(txtRecordId);

        JPanel form = new JPanel(new GridLayout(1, 2, 10, 10));
        form.setOpaque(false);
        form.add(labeled("Medical Record ID", txtRecordId));
        form.add(new JLabel());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton create = new JButton("Tạo từ hồ sơ");
        JButton delete = new JButton("Xóa");
        JButton refresh = new JButton("Làm mới");
        HmsTheme.styleSecondaryButton(create);
        HmsTheme.styleSecondaryButton(delete);
        HmsTheme.styleSecondaryButton(refresh);

        create.addActionListener(e -> onCreate());
        delete.addActionListener(e -> onDelete());
        refresh.addActionListener(e -> refreshTable());

        actions.add(create);
        actions.add(delete);
        actions.add(refresh);

        card.add(form, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private JComponent tableCard() {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel h = new JLabel("Danh sách đơn thuốc");
        h.setFont(HmsTheme.fontBold(14));
        h.setForeground(HmsTheme.TEXT);

        HmsTheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> fillFromSelection());

        card.add(h, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private void onCreate() {
        try {
            Long recordId = Long.parseLong(txtRecordId.getText().trim());
            // UI only delegates; validation is in service
            MedicalRecord record = medicalRecordService.getMedicalRecordById(recordId);
            if (record == null) {
                throw new RuntimeException("Hồ sơ bệnh án không tồn tại!");
            }
            prescriptionService.createFromMedicalRecord(recordId);
            refreshTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 đơn thuốc để xóa.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa đơn thuốc đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            Long id = Long.parseLong(tableModel.getValueAt(row, 0).toString());
            prescriptionService.deletePrescription(id);
            refreshTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Prescription> list = prescriptionService.getAllPrescriptions();
        if (list == null) return;
        for (Prescription p : list) {
            Long recordId = p.getMedicalRecord() != null ? p.getMedicalRecord().getId() : null;
            tableModel.addRow(new Object[]{p.getId(), recordId, p.getCreatedAt()});
        }
    }

    private void fillFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Object recordId = tableModel.getValueAt(row, 1);
        txtRecordId.setText(recordId == null ? "" : recordId.toString());
    }

    private void clearForm() {
        txtRecordId.setText("");
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

