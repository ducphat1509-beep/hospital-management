package com.hms.dao;

import com.hms.model.entity.Patient;
import java.util.List;

public interface PatientDAO {
    List<Patient> findAll();
    Patient findById(Long id);
    void save(Patient patient);
    void delete(Long id);
}