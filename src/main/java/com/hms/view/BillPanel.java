package com.hms.view;

import com.hms.model.dto.UserSessionDTO;
import com.hms.model.entity.Bill;
import com.hms.model.entity.BillMedicineDetail;
import com.hms.model.entity.Medicine;
import com.hms.model.entity.Patient;
import com.hms.service.BillMedicineDetailService;
import com.hms.service.BillService;
import com.hms.service.MedicineService;
import com.hms.service.PatientService;
import com.hms.view.ui.HmsTheme;
import com.hms.view.ui.RoundedPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.hms.model.entity.Account;
import com.hms.model.enumtype.UserRole;

public class BillPanel extends JPanel {

    private final BillService billService;
    private final BillMedicineDetailService billMedicineDetailService;
    private final MedicineService medicineService;
    private final PatientService patientService;
    private final UserSessionDTO session;

    private final JComboBox<Patient> cbPatient = new JComboBox<>();
    private final JComboBox<Medicine> cbMedicine = new JComboBox<>();
    private final JTextField txtQuantity = new JTextField("1");

    private final DefaultTableModel billModel = new DefaultTableModel(
            new Object[]{"Bill ID", "Patient", "Total", "Status"}, 0
    );
    private final JTable billTable = new JTable(billModel);

    private final DefaultTableModel lineModel = new DefaultTableModel(
            new Object[]{"Line ID", "Medicine", "Qty", "Unit Price"}, 0
    );
    private final JTable lineTable = new JTable(lineModel);

    public BillPanel(
            BillService billService,
            BillMedicineDetailService billMedicineDetailService,
            MedicineService medicineService,
            PatientService patientService,
            UserSessionDTO session
    ) {
        this.billService = billService;
        this.billMedicineDetailService = billMedicineDetailService;
        this.medicineService = medicineService;
        this.patientService = patientService;
        this.session = session;

        setOpaque(false);
        setLayout(new BorderLayout(16, 16));
        add(header("Hóa đơn"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 16));
        body.setOpaque(false);
        body.add(actionsCard(), BorderLayout.NORTH);
        body.add(tables(), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        loadCombos();
        refreshBills();
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

    private JComponent actionsCard() {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        txtQuantity.setFont(HmsTheme.fontRegular(12));
        txtQuantity.setBorder(HmsTheme.roundedLineBorder(16));

        cbPatient.setFont(HmsTheme.fontRegular(12));
        cbMedicine.setFont(HmsTheme.fontRegular(12));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row1.setOpaque(false);
        row1.add(new JLabel("Bệnh nhân:"));
        row1.add(cbPatient);
        JButton createBill = new JButton("Tạo hóa đơn");
        HmsTheme.styleSecondaryButton(createBill);
        createBill.addActionListener(e -> onCreateBill());
        row1.add(createBill);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row2.setOpaque(false);
        row2.add(new JLabel("Thuốc:"));
        row2.add(cbMedicine);
        row2.add(new JLabel("Số lượng:"));
        row2.add(txtQuantity);

        JButton addLine = new JButton("Thêm thuốc");
        JButton deleteLine = new JButton("Xóa dòng");
        JButton recalc = new JButton("Tính lại tổng");
        HmsTheme.styleSecondaryButton(addLine);
        HmsTheme.styleSecondaryButton(deleteLine);
        HmsTheme.styleSecondaryButton(recalc);

        addLine.addActionListener(e -> onAddLine());
        deleteLine.addActionListener(e -> onDeleteLine());
        recalc.addActionListener(e -> onRecalc());

        row2.add(addLine);
        row2.add(deleteLine);
        row2.add(recalc);

        card.add(row1, BorderLayout.NORTH);
        card.add(row2, BorderLayout.CENTER);
        return card;
    }

    private JComponent tables() {
        JPanel wrap = new JPanel(new GridLayout(1, 2, 16, 16));
        wrap.setOpaque(false);
        wrap.add(billTableCard());
        wrap.add(lineTableCard());
        return wrap;
    }

    private JComponent billTableCard() {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel h = new JLabel("Danh sách hóa đơn");
        h.setFont(HmsTheme.fontBold(14));
        h.setForeground(HmsTheme.TEXT);

        HmsTheme.styleTable(billTable);
        billTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        billTable.getSelectionModel().addListSelectionListener(e -> refreshLinesForSelectedBill());

        card.add(h, BorderLayout.NORTH);
        card.add(new JScrollPane(billTable), BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);
        if (session.getRole() == UserRole.RECEPTIONIST || session.getRole() == UserRole.ADMIN) {
            JButton btnPaid = new JButton("Đã thanh toán (PAID)");
            HmsTheme.styleSecondaryButton(btnPaid);
            btnPaid.addActionListener(e -> updateBillStatus(Bill.BillStatus.PAID));
            actionPanel.add(btnPaid);
            card.add(actionPanel, BorderLayout.SOUTH);
        }
        
        return card;
    }

    private JComponent lineTableCard() {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel h = new JLabel("Thuốc trong hóa đơn");
        h.setFont(HmsTheme.fontBold(14));
        h.setForeground(HmsTheme.TEXT);

        HmsTheme.styleTable(lineTable);
        lineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        card.add(h, BorderLayout.NORTH);
        card.add(new JScrollPane(lineTable), BorderLayout.CENTER);
        return card;
    }

    private void loadCombos() {
        cbPatient.removeAllItems();
        List<Patient> patients = patientService.getAllPatients();
        if (patients != null) patients.forEach(cbPatient::addItem);
        cbPatient.setSelectedIndex(-1);

        cbMedicine.removeAllItems();
        List<Medicine> meds = medicineService.getAllMedicines();
        if (meds != null) meds.forEach(cbMedicine::addItem);
        cbMedicine.setSelectedIndex(-1);
    }

    private void onCreateBill() {
        try {
            Patient p = (Patient) cbPatient.getSelectedItem();
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bệnh nhân.");
                return;
            }
            Bill bill = billService.createBill(p.getId());
            refreshBills();
            selectBill(bill.getId());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onAddLine() {
        Long billId = selectedBillId();
        if (billId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hóa đơn.");
            return;
        }
        try {
            Medicine m = (Medicine) cbMedicine.getSelectedItem();
            if (m == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thuốc.");
                return;
            }
            int qty = Integer.parseInt(txtQuantity.getText().trim());
            billMedicineDetailService.addMedicineToBill(billId, m.getId(), qty);
            refreshLinesForSelectedBill();
            refreshBills();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onDeleteLine() {
        int row = lineTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng để xóa.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa dòng đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            Long id = Long.parseLong(lineModel.getValueAt(row, 0).toString());
            billMedicineDetailService.deleteDetail(id);
            refreshLinesForSelectedBill();
            refreshBills();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onRecalc() {
        Long billId = selectedBillId();
        if (billId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hóa trường.");
            return;
        }
        try {
            billService.calculateTotalAmount(billId);
            refreshBills();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void updateBillStatus(Bill.BillStatus status) {
        Long billId = selectedBillId();
        if (billId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hóa đơn để cập nhật!");
            return;
        }
        try {
            billService.updateBillStatus(billId, status);
            refreshBills();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void refreshBills() {
        billModel.setRowCount(0);
        List<Bill> bills = billService.getAllBills();
        if (bills == null) return;
        for (Bill b : bills) {
            String patientName = b.getPatient() != null ? b.getPatient().getFullName() : "";
            billModel.addRow(new Object[]{b.getId(), patientName, b.getTotalAmount(), b.getStatus()});
        }
    }

    private void refreshLinesForSelectedBill() {
        lineModel.setRowCount(0);
        Long billId = selectedBillId();
        if (billId == null) return;
        // Minimal: we show what service returns via getAllDetails and filter in UI (no business logic)
        List<BillMedicineDetail> list = billMedicineDetailService.getAllDetails();
        if (list == null) return;
        for (BillMedicineDetail d : list) {
            if (d.getBill() == null || d.getBill().getId() == null) continue;
            if (!billId.equals(d.getBill().getId())) continue;
            String medName = d.getMedicine() != null ? d.getMedicine().getName() : "";
            lineModel.addRow(new Object[]{d.getId(), medName, d.getQuantity(), d.getPrice()});
        }
    }

    private Long selectedBillId() {
        int row = billTable.getSelectedRow();
        if (row < 0) return null;
        return Long.parseLong(billModel.getValueAt(row, 0).toString());
    }

    private void selectBill(Long billId) {
        for (int i = 0; i < billModel.getRowCount(); i++) {
            if (billId.toString().equals(billModel.getValueAt(i, 0).toString())) {
                billTable.setRowSelectionInterval(i, i);
                break;
            }
        }
    }
}

