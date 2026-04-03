package com.hms.controller;

import com.hms.model.entity.Prescription;
import com.hms.service.PrescriptionService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public List<Prescription> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionService.getPrescriptionById(id);
    }

    public Prescription createFromMedicalRecord(Long medicalRecordId) {
        return prescriptionService.createFromMedicalRecord(medicalRecordId);
    }

    public void savePrescription(Prescription prescription) {
        prescriptionService.savePrescription(prescription);
    }

    public void deletePrescription(Long id) {
        prescriptionService.deletePrescription(id);
    }
}
