package com.hms.dao;

import com.hms.model.entity.MedicalRecord;
import java.util.List;

public interface MedicalRecordDAO {
    List<MedicalRecord> findAll();
    MedicalRecord findById(Long id);
    MedicalRecord findByAppointmentId(Long appointmentId);
    void save(MedicalRecord medicalRecord);
    void delete(Long id);
}
