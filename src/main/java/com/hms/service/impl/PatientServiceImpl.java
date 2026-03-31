package com.hms.service.impl;

import com.hms.dao.PatientDAO;
import com.hms.model.entity.Patient;
import com.hms.service.PatientService;
import java.util.List;

public class PatientServiceImpl implements PatientService {

    // Service cầm "remote" điều khiển DAO
    private final PatientDAO patientDAO;

    // Constructor: Khi tạo Service, ta đưa cho nó một ông DAO cụ thể
    public PatientServiceImpl(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientDAO.findAll();
    }

    @Override
    public void registerPatient(Patient patient) {
        // Đây là nơi thực hiện logic nghiệp vụ
        if (patient.getPhone() == null || patient.getPhone().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống!");
        }

        // Nếu mọi thứ ổn, mới gọi DAO để lưu
        patientDAO.save(patient);
        System.out.println("Nghiệp vụ: Đã đăng ký bệnh nhân " + patient.getFullName());
    }

    @Override
    public void updatePatientInfo(Patient patient) {
        patientDAO.save(patient); // Trong Hibernate, persist/merge đều dùng save
    }

    @Override
    public void removePatient(Long id) {
        patientDAO.delete(id);
    }
}