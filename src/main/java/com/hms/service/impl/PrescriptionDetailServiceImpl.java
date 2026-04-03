package com.hms.service.impl;

import com.hms.dao.MedicineDAO;
import com.hms.dao.PrescriptionDAO;
import com.hms.dao.PrescriptionDetailDAO;
import com.hms.model.entity.Medicine;
import com.hms.model.entity.Prescription;
import com.hms.model.entity.PrescriptionDetail;
import com.hms.service.PrescriptionDetailService;

import java.util.List;

public class PrescriptionDetailServiceImpl implements PrescriptionDetailService {

    private final PrescriptionDetailDAO prescriptionDetailDAO;
    private final PrescriptionDAO prescriptionDAO;
    private final MedicineDAO medicineDAO;

    public PrescriptionDetailServiceImpl(
            PrescriptionDetailDAO prescriptionDetailDAO,
            PrescriptionDAO prescriptionDAO,
            MedicineDAO medicineDAO
    ) {
        this.prescriptionDetailDAO = prescriptionDetailDAO;
        this.prescriptionDAO = prescriptionDAO;
        this.medicineDAO = medicineDAO;
    }

    @Override
    public List<PrescriptionDetail> getAllDetails() {
        return prescriptionDetailDAO.findAll();
    }

    @Override
    public PrescriptionDetail getDetailById(Long id) {
        PrescriptionDetail d = prescriptionDetailDAO.findById(id);
        if (d == null) {
            throw new RuntimeException("Không tìm thấy chi tiết đơn thuốc với ID: " + id);
        }
        return d;
    }

    @Override
    public PrescriptionDetail addMedicineToPrescription(Long prescriptionId, Long medicineId, int quantity, String dosage) {
        if (prescriptionId == null || medicineId == null) {
            throw new RuntimeException("Đơn thuốc và thuốc không được để trống!");
        }
        if (quantity <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0!");
        }

        Prescription prescription = prescriptionDAO.findById(prescriptionId);
        if (prescription == null) {
            throw new RuntimeException("Đơn thuốc không tồn tại!");
        }

        Medicine medicine = medicineDAO.findById(medicineId);
        if (medicine == null) {
            throw new RuntimeException("Thuốc không tồn tại!");
        }

        int stock = medicine.getStockQuantity() != null ? medicine.getStockQuantity() : 0;
        PrescriptionDetail existing = prescriptionDetailDAO.findByPrescriptionAndMedicine(prescriptionId, medicineId);
        int requiredTotal = existing == null ? quantity : existing.getQuantity() + quantity;
        if (requiredTotal > stock) {
            throw new RuntimeException("Số lượng vượt quá tồn kho (còn " + stock + ")!");
        }

        if (existing != null) {
            existing.setQuantity(requiredTotal);
            existing.setDosage(dosage);
            prescriptionDetailDAO.save(existing);
            return existing;
        }

        PrescriptionDetail detail = new PrescriptionDetail();
        detail.setPrescription(prescription);
        detail.setMedicine(medicine);
        detail.setQuantity(quantity);
        detail.setDosage(dosage);
        prescriptionDetailDAO.save(detail);
        return detail;
    }

    @Override
    public void deleteDetail(Long id) {
        if (prescriptionDetailDAO.findById(id) == null) {
            throw new RuntimeException("Không tìm thấy chi tiết để xóa!");
        }
        prescriptionDetailDAO.delete(id);
    }
}
