package com.hms.service;

import com.hms.model.entity.MedicalRecord;
import java.util.List;

public interface MedicalRecordService {

    List<MedicalRecord> getAllMedicalRecords();

    MedicalRecord getMedicalRecordById(Long id);

    /**
     * Creates a medical record linked to the given appointment (one record per appointment).
     */
    MedicalRecord createFromAppointment(Long appointmentId, String diagnosis, String notes);

    void updateMedicalRecord(MedicalRecord medicalRecord);

    void deleteMedicalRecord(Long id);
}
