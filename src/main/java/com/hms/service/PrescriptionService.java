package com.hms.service;

import com.hms.model.entity.Prescription;
import java.util.List;

public interface PrescriptionService {

    List<Prescription> getAllPrescriptions();

    Prescription getPrescriptionById(Long id);

    /**
     * Creates a prescription for the given medical record (one prescription per record).
     */
    Prescription createFromMedicalRecord(Long medicalRecordId);

    void savePrescription(Prescription prescription);

    void deletePrescription(Long id);
}
