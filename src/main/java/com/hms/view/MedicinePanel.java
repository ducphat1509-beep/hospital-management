package com.hms.view;

import com.hms.model.entity.Medicine;
import com.hms.service.MedicineService;
import com.hms.view.ui.HmsTheme;
import com.hms.view.ui.RoundedPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class MedicinePanel extends JPanel {

    private final MedicineService medicineService;

    private final JTextField txtName = new JTextField();
    private final JTextField txtPrice = new JTextField();
    private final JTextField txtStock = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Tên thuốc", "Giá", "Tồn kho"}, 0
    );
    private final JTable table = new JTable(tableModel);

    public MedicinePanel(MedicineService medicineService) {
        this.medicineService = medicineService;

        setOpaque(false);
        setLayout(new BorderLayout(16, 16));

        add(header("Thuốc"), BorderLayout.NORTH);

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

        styleField(txtName);
        styleField(txtPrice);
        styleField(txtStock);

        JPanel form = new JPanel(new GridLayout(1, 3, 10, 10));
        form.setOpaque(false);
        form.add(labeled("Tên thuốc", txtName));
        form.add(labeled("Giá (vd: 12.50)", txtPrice));
        form.add(labeled("Tồn kho", txtStock));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton add = new JButton("Thêm");
        JButton update = new JButton("Cập nhật");
        JButton delete = new JButton("Xóa");
        JButton restock = new JButton("Nhập kho");
        JButton issue = new JButton("Xuất kho");
        HmsTheme.styleSecondaryButton(add);
        HmsTheme.styleSecondaryButton(update);
        HmsTheme.styleSecondaryButton(delete);
        HmsTheme.styleSecondaryButton(restock);
        HmsTheme.styleSecondaryButton(issue);

        add.addActionListener(e -> onAdd());
        update.addActionListener(e -> onUpdate());
        delete.addActionListener(e -> onDelete());
        restock.addActionListener(e -> onAdjust(true));
        issue.addActionListener(e -> onAdjust(false));

        actions.add(add);
        actions.add(update);
        actions.add(delete);
        actions.add(restock);
        actions.add(issue);

        card.add(form, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private JComponent tableCard() {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel h = new JLabel("Danh sách thuốc");
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
            Medicine m = new Medicine();
            m.setName(txtName.getText());
            m.setPrice(new BigDecimal(txtPrice.getText().trim()));
            String stockRaw = txtStock.getText() != null ? txtStock.getText().trim() : "";
            m.setStockQuantity(stockRaw.isEmpty() ? 0 : Integer.parseInt(stockRaw));
            medicineService.createMedicine(m);
            refreshTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onUpdate() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 thuốc để cập nhật.");
            return;
        }
        try {
            Long id = Long.parseLong(tableModel.getValueAt(row, 0).toString());
            Medicine m = new Medicine();
            m.setId(id);
            m.setName(txtName.getText());
            m.setPrice(new BigDecimal(txtPrice.getText().trim()));
            String stockRaw = txtStock.getText() != null ? txtStock.getText().trim() : "";
            m.setStockQuantity(stockRaw.isEmpty() ? 0 : Integer.parseInt(stockRaw));
            medicineService.updateMedicine(m);
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 thuốc để xóa.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa thuốc đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            Long id = Long.parseLong(tableModel.getValueAt(row, 0).toString());
            medicineService.deleteMedicine(id);
            refreshTable();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onAdjust(boolean increase) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 thuốc để thao tác kho.");
            return;
        }
        String label = increase ? "Nhập kho" : "Xuất kho";
        String raw = JOptionPane.showInputDialog(this, label + " - Số lượng:", "0");
        if (raw == null) return;
        try {
            int amount = Integer.parseInt(raw.trim());
            Long id = Long.parseLong(tableModel.getValueAt(row, 0).toString());
            if (increase) {
                medicineService.increaseStock(id, amount);
            } else {
                medicineService.decreaseStock(id, amount);
            }
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Medicine> list = medicineService.getAllMedicines();
        if (list == null) return;
        for (Medicine m : list) {
            tableModel.addRow(new Object[]{m.getId(), m.getName(), m.getPrice(), m.getStockQuantity()});
        }
    }

    private void fillFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtName.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtPrice.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtStock.setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }

    private void clearForm() {
        txtName.setText("");
        txtPrice.setText("");
        txtStock.setText("");
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

