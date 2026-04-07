package com.hms.service;

import com.hms.model.entity.Doctor;
import java.util.List;

public interface DoctorService {
    List<Doctor> getAllDoctors();
    Doctor getDoctorById(Long id);
        void createDoctor(Doctor doctor);
        void updateDoctor(Doctor doctor);
        void deleteDoctor(Long id);
    }