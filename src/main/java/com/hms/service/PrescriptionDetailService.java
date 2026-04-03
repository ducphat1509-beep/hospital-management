package com.hms.service;

import com.hms.model.entity.PrescriptionDetail;
import java.util.List;

public interface PrescriptionDetailService {

    List<PrescriptionDetail> getAllDetails();

    PrescriptionDetail getDetailById(Long id);

    /**
     * Adds a medicine line with quantity validation (positive, not above available stock).
     * Merges quantity if the same medicine already exists on the prescription.
     */
    PrescriptionDetail addMedicineToPrescription(Long prescriptionId, Long medicineId, int quantity, String dosage);

    void deleteDetail(Long id);
}
