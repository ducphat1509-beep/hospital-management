package com.hms.controller;

import com.hms.model.entity.Patient;
import com.hms.service.PatientService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    public void addPatient(Patient patient) {
        patientService.registerPatient(patient);
    }

    public void updatePatient(Patient patient) {
        patientService.updatePatientInfo(patient);
    }


    public void deletePatient(Long id) {
        patientService.removePatient(id);
    }
}