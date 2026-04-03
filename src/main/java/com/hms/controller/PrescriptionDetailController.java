package com.hms.controller;

import com.hms.model.entity.PrescriptionDetail;
import com.hms.service.PrescriptionDetailService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PrescriptionDetailController {

    private final PrescriptionDetailService prescriptionDetailService;

    public List<PrescriptionDetail> getAllDetails() {
        return prescriptionDetailService.getAllDetails();
    }

    public PrescriptionDetail getDetailById(Long id) {
        return prescriptionDetailService.getDetailById(id);
    }

    public PrescriptionDetail addMedicine(Long prescriptionId, Long medicineId, int quantity, String dosage) {
        return prescriptionDetailService.addMedicineToPrescription(prescriptionId, medicineId, quantity, dosage);
    }

    public void deleteDetail(Long id) {
        prescriptionDetailService.deleteDetail(id);
    }
}

