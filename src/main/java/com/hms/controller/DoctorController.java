package com.hms.controller;

import com.hms.model.entity.Doctor;
import com.hms.service.DoctorService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    public void addDoctor(Doctor doctor) {
        doctorService.createDoctor(doctor);
    }

    public void deleteDoctor(Long id) {
        doctorService.deleteDoctor(id);
    }
}