package com.hms.controller;

import com.hms.model.entity.MedicalRecord;
import com.hms.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    public MedicalRecord getMedicalRecordById(Long id) {
        return medicalRecordService.getMedicalRecordById(id);
    }

    public MedicalRecord createFromAppointment(Long appointmentId, String diagnosis, String notes) {
        return medicalRecordService.createFromAppointment(appointmentId, diagnosis, notes);
    }

    public void updateMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecordService.updateMedicalRecord(medicalRecord);
    }

    public void deleteMedicalRecord(Long id) {
        medicalRecordService.deleteMedicalRecord(id);
    }
}
