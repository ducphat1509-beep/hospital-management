package com.hms.service;

import com.hms.model.entity.Patient;
import java.util.List;

public interface PatientService {
    List<Patient> getAllPatients();
    void registerPatient(Patient patient); // Nghiệp vụ đăng ký bệnh nhân mới
    void updatePatientInfo(Patient patient);
    void removePatient(Long id);
}