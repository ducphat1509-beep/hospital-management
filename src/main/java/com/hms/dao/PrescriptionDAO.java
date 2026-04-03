package com.hms.dao;

import com.hms.model.entity.Prescription;
import java.util.List;

public interface PrescriptionDAO {
    List<Prescription> findAll();
    Prescription findById(Long id);
    Prescription findByMedicalRecordId(Long medicalRecordId);
    void save(Prescription prescription);
    void delete(Long id);
}
