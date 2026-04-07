package com.hms.view;

import com.hms.model.entity.Appointment;
import com.hms.model.entity.MedicalRecord;
import com.hms.model.entity.Medicine;
import com.hms.model.entity.Prescription;
import com.hms.model.entity.PrescriptionDetail;
import com.hms.service.AppointmentService;
import com.hms.service.MedicalRecordService;
import com.hms.service.MedicineService;
import com.hms.service.PrescriptionDetailService;
import com.hms.service.PrescriptionService;
import com.hms.view.ui.HmsTheme;
import com.hms.view.ui.RoundedPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrescriptionPanel extends JPanel {

    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;
    private final PrescriptionService prescriptionService;
    private final MedicineService medicineService;
    private final PrescriptionDetailService prescriptionDetailService;

    // UI Components - Record
    private final JComboBox<Appointment> cbAppointment = new JComboBox<>();
    private final JTextArea txtDiagnosis = new JTextArea(3, 20);
    private final JTextArea txtNotes = new JTextArea(3, 20);
    private final JButton btnSaveRecord = new JButton("Lưu Hồ Sơ");

    // UI Components - Prescription
    private final JComboBox<Medicine> cbMedicine = new JComboBox<>();
    private final JTextField txtQuantity = new JTextField();
    private final JTextField txtDosage = new JTextField();
    private final JButton btnAddMedicine = new JButton("Thêm Thuốc");

    // State
    private MedicalRecord currentRecord = null;
    private Prescription currentPrescription = null;

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Tên Thuốc", "Số lượng", "Liều dùng"}, 0
    );
    private final JTable table = new JTable(tableModel);

    public PrescriptionPanel(
            AppointmentService appointmentService,
            MedicalRecordService medicalRecordService,
            PrescriptionService prescriptionService,
            MedicineService medicineService,
            PrescriptionDetailService prescriptionDetailService) {
        
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
        this.prescriptionService = prescriptionService;
        this.medicineService = medicineService;
        this.prescriptionDetailService = prescriptionDetailService;

        setOpaque(false);
        setLayout(new BorderLayout(16, 16));

        add(header("Khám Bệnh & Đơn Thuốc"), BorderLayout.NORTH);

        JPanel mainLayout = new JPanel(new GridLayout(1, 2, 16, 16));
        mainLayout.setOpaque(false);
        
        mainLayout.add(buildLeftPanel());
        mainLayout.add(buildRightPanel());

        add(mainLayout, BorderLayout.CENTER);

        loadInitialData();
        setupListeners();
        updateUIState();
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

    private JComponent buildLeftPanel() {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel h = new JLabel("Hồ Sơ Bệnh Án");
        h.setFont(HmsTheme.fontBold(14));
        h.setForeground(HmsTheme.TEXT);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        cbAppointment.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Appointment) {
                    Appointment a = (Appointment) value;
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM HH:mm");
                    setText("ID: " + a.getId() + " - " + a.getPatient().getFullName() + " - " + (a.getAppointmentTime() != null ? a.getAppointmentTime().format(fmt) : ""));
                }
                return this;
            }
        });
        
        txtDiagnosis.setFont(HmsTheme.fontRegular(12));
        txtDiagnosis.setBorder(HmsTheme.roundedLineBorder(8));
        txtNotes.setFont(HmsTheme.fontRegular(12));
        txtNotes.setBorder(HmsTheme.roundedLineBorder(8));

        gbc.gridy = 0; form.add(new JLabel("Chọn Lịch Hẹn:"), gbc);
        gbc.gridy = 1; form.add(cbAppointment, gbc);
        
        gbc.gridy = 2; form.add(new JLabel("Chẩn đoán:"), gbc);
        gbc.gridy = 3; form.add(new JScrollPane(txtDiagnosis), gbc);
        
        gbc.gridy = 4; form.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridy = 5; form.add(new JScrollPane(txtNotes), gbc);

        HmsTheme.styleSecondaryButton(btnSaveRecord);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        JButton btnRefresh = new JButton("Làm mới");
        HmsTheme.styleSecondaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadInitialData());

        actions.add(btnRefresh);
        actions.add(btnSaveRecord);

        card.add(h, BorderLayout.NORTH);
        card.add(form, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);

        return card;
    }

    private JComponent buildRightPanel() {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel h = new JLabel("Kê Đơn Thuốc");
        h.setFont(HmsTheme.fontBold(14));
        h.setForeground(HmsTheme.TEXT);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridx = 0; gbc.weightx = 1.0;

        txtQuantity.setFont(HmsTheme.fontRegular(12));
        txtQuantity.setBorder(HmsTheme.roundedLineBorder(8));
        txtDosage.setFont(HmsTheme.fontRegular(12));
        txtDosage.setBorder(HmsTheme.roundedLineBorder(8));

        gbc.gridy = 0; form.add(new JLabel("Thuốc:"), gbc);
        gbc.gridy = 1; form.add(cbMedicine, gbc);
        
        gbc.gridy = 2; form.add(new JLabel("Số lượng:"), gbc);
        gbc.gridy = 3; form.add(txtQuantity, gbc);
        
        gbc.gridy = 4; form.add(new JLabel("Liều dùng:"), gbc);
        gbc.gridy = 5; form.add(txtDosage, gbc);

        HmsTheme.styleSecondaryButton(btnAddMedicine);
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addPanel.setOpaque(false);
        addPanel.add(btnAddMedicine);
        
        gbc.gridy = 6; form.add(addPanel, gbc);

        HmsTheme.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 150));
        
        JPanel topHalf = new JPanel(new BorderLayout());
        topHalf.setOpaque(false);
        topHalf.add(h, BorderLayout.NORTH);
        topHalf.add(form, BorderLayout.CENTER);

        card.add(topHalf, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        JButton btnDelete = new JButton("Xóa thuốc chọn");
        HmsTheme.styleSecondaryButton(btnDelete);
        btnDelete.addActionListener(e -> onDeleteMedicine());
        actions.add(btnDelete);
        card.add(actions, BorderLayout.SOUTH);

        return card;
    }

    private void loadInitialData() {
        cbAppointment.removeAllItems();
        List<Appointment> apps = appointmentService.getAppointmentsByDoctor(null);
        if (apps != null) {
            apps.stream()
                .filter(a -> a.getStatus() == Appointment.AppointmentStatus.CONFIRMED || a.getStatus() == Appointment.AppointmentStatus.DONE)
                .forEach(cbAppointment::addItem);
        }
        cbAppointment.setSelectedIndex(-1);

        cbMedicine.removeAllItems();
        List<Medicine> meds = medicineService.getAllMedicines();
        if (meds != null) {
            meds.forEach(cbMedicine::addItem);
        }
        cbMedicine.setSelectedIndex(-1);

        currentRecord = null;
        currentPrescription = null;
        updateUIState();
        refreshTable();
    }

    private void setupListeners() {
        cbAppointment.addActionListener(e -> onAppointmentSelected());
        btnSaveRecord.addActionListener(e -> onSaveRecord());
        btnAddMedicine.addActionListener(e -> onAddMedicine());
    }

    private void onAppointmentSelected() {
        Appointment a = (Appointment) cbAppointment.getSelectedItem();
        if (a == null) {
            currentRecord = null;
            currentPrescription = null;
            txtDiagnosis.setText("");
            txtNotes.setText("");
            updateUIState();
            refreshTable();
            return;
        }

        List<MedicalRecord> allRecords = medicalRecordService.getAllMedicalRecords();
        currentRecord = null;
        for (MedicalRecord r : allRecords) {
            if (r.getAppointment().getId().equals(a.getId())) {
                currentRecord = r;
                break;
            }
        }

        if (currentRecord != null) {
            txtDiagnosis.setText(currentRecord.getDiagnosis());
            txtNotes.setText(currentRecord.getNotes());
            
            // Tìm Prescription của record này
            List<Prescription> allPrescriptions = prescriptionService.getAllPrescriptions();
            currentPrescription = null;
            for (Prescription p : allPrescriptions) {
                if (p.getMedicalRecord().getId().equals(currentRecord.getId())) {
                    currentPrescription = p;
                    break;
                }
            }
            // Nếu chưa có đơn thuốc, tự động tạo mới
            if (currentPrescription == null) {
                try {
                    currentPrescription = prescriptionService.createFromMedicalRecord(currentRecord.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            txtDiagnosis.setText("");
            txtNotes.setText("");
            currentPrescription = null;
        }

        updateUIState();
        refreshTable();
    }

    private void onSaveRecord() {
        Appointment a = (Appointment) cbAppointment.getSelectedItem();
        if (a == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Lịch Hẹn!");
            return;
        }

        String diagnosis = txtDiagnosis.getText().trim();
        String notes = txtNotes.getText().trim();

        try {
            if (currentRecord == null) {
                currentRecord = medicalRecordService.createFromAppointment(a.getId(), diagnosis, notes);
                currentPrescription = prescriptionService.createFromMedicalRecord(currentRecord.getId());
                JOptionPane.showMessageDialog(this, "Đã tạo hồ sơ bệnh án thành công!");
            } else {
                currentRecord.setDiagnosis(diagnosis);
                currentRecord.setNotes(notes);
                medicalRecordService.updateMedicalRecord(currentRecord);
                JOptionPane.showMessageDialog(this, "Đã cập nhật hồ sơ bệnh án!");
            }
            updateUIState();
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onAddMedicine() {
        if (currentPrescription == null) {
            JOptionPane.showMessageDialog(this, "Chưa có Đơn Thuốc. Vui lòng tạo Hồ sơ bệnh án trước!");
            return;
        }

        Medicine m = (Medicine) cbMedicine.getSelectedItem();
        if (m == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Thuốc!");
            return;
        }

        try {
            int qty = Integer.parseInt(txtQuantity.getText().trim());
            String dosage = txtDosage.getText().trim();
            prescriptionDetailService.addMedicineToPrescription(currentPrescription.getId(), m.getId(), qty, dosage);
            txtQuantity.setText("");
            txtDosage.setText("");
            refreshTable();
        } catch(NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên hợp lệ!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void onDeleteMedicine() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thuốc trong đơn để xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn xóa thuốc này khỏi đơn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Long detailId = Long.parseLong(tableModel.getValueAt(row, 0).toString());
            prescriptionDetailService.deleteDetail(detailId);
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void updateUIState() {
        boolean recordExists = currentRecord != null;
        boolean hasAppointment = cbAppointment.getSelectedItem() != null;
        
        txtDiagnosis.setEnabled(hasAppointment);
        txtNotes.setEnabled(hasAppointment);
        btnSaveRecord.setEnabled(hasAppointment);

        boolean canPrescribe = currentPrescription != null;
        cbMedicine.setEnabled(canPrescribe);
        txtQuantity.setEnabled(canPrescribe);
        txtDosage.setEnabled(canPrescribe);
        btnAddMedicine.setEnabled(canPrescribe);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        if (currentPrescription == null) return;

        List<PrescriptionDetail> details = prescriptionDetailService.getAllDetails();
        if (details != null) {
            details.stream()
                   .filter(d -> d.getPrescription().getId().equals(currentPrescription.getId()))
                   .forEach(d -> {
                        String medName = d.getMedicine() != null ? d.getMedicine().getName() : "";
                        tableModel.addRow(new Object[]{d.getId(), medName, d.getQuantity(), d.getDosage()});
                   });
        }
    }
}
