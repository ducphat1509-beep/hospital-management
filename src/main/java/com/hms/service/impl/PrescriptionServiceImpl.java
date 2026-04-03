package com.hms.service.impl;

import com.hms.dao.MedicalRecordDAO;
import com.hms.dao.PrescriptionDAO;
import com.hms.model.entity.MedicalRecord;
import com.hms.model.entity.Prescription;
import com.hms.service.PrescriptionService;

import java.util.List;

public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionDAO prescriptionDAO;
    private final MedicalRecordDAO medicalRecordDAO;

    public PrescriptionServiceImpl(PrescriptionDAO prescriptionDAO, MedicalRecordDAO medicalRecordDAO) {
        this.prescriptionDAO = prescriptionDAO;
        this.medicalRecordDAO = medicalRecordDAO;
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return prescriptionDAO.findAll();
    }

    @Override
    public Prescription getPrescriptionById(Long id) {
        Prescription p = prescriptionDAO.findById(id);
        if (p == null) {
            throw new RuntimeException("Không tìm thấy đơn thuốc với ID: " + id);
        }
        return p;
    }

    @Override
    public Prescription createFromMedicalRecord(Long medicalRecordId) {
        if (medicalRecordId == null) {
            throw new RuntimeException("Mã hồ sơ bệnh án không được để trống!");
        }

        MedicalRecord record = medicalRecordDAO.findById(medicalRecordId);
        if (record == null) {
            throw new RuntimeException("Hồ sơ bệnh án không tồn tại!");
        }
        if (prescriptionDAO.findByMedicalRecordId(medicalRecordId) != null) {
            throw new RuntimeException("Hồ sơ này đã có đơn thuốc!");
        }

        Prescription prescription = new Prescription();
        prescription.setMedicalRecord(record);
        prescriptionDAO.save(prescription);
        return prescription;
    }

    @Override
    public void savePrescription(Prescription prescription) {
        if (prescription == null) {
            throw new RuntimeException("Đơn thuốc không hợp lệ!");
        }
        if (prescription.getId() != null && prescriptionDAO.findById(prescription.getId()) == null) {
            throw new RuntimeException("Đơn thuốc không tồn tại!");
        }
        prescriptionDAO.save(prescription);
    }

    @Override
    public void deletePrescription(Long id) {
        if (prescriptionDAO.findById(id) == null) {
            throw new RuntimeException("Không tìm thấy đơn thuốc để xóa!");
        }
        prescriptionDAO.delete(id);
    }
}
