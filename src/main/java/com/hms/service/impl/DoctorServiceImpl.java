package com.hms.service.impl;

import com.hms.dao.DoctorDAO;
import com.hms.model.entity.Doctor;
import com.hms.service.DoctorService;
import lombok.RequiredArgsConstructor; // (1) Dùng Lombok để tự tạo Constructor

import java.util.List;

@RequiredArgsConstructor // (2) Tự động tạo constructor cho các field 'final'
public class DoctorServiceImpl implements DoctorService {

    // Khai báo 'final' để Lombok biết cần đưa vào Constructor
    private final DoctorDAO doctorDAO;

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorDAO.findAll();
    }

    @Override
    public Doctor getDoctorById(Long id) {
        Doctor doctor = doctorDAO.findById(id);
        if (doctor == null) {
            throw new RuntimeException("Không tìm thấy bác sĩ với ID: " + id);
        }
        return doctor;
    }

    @Override
    public void createDoctor(Doctor doctor) {
        // Validation nghiệp vụ cơ bản
        if (doctor.getFullName() == null || doctor.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Tên bác sĩ không được để trống!");
        }
        doctorDAO.save(doctor);
    }

    @Override
    public void updateDoctor(Doctor doctor) {
        if (doctor.getFullName() == null || doctor.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Tên bác sĩ không được để trống!");
        }
        if (doctor.getId() == null) {
            throw new RuntimeException("ID bác sĩ không hợp lệ!");
        }
        doctorDAO.save(doctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        // Kiểm tra xem bác sĩ có tồn tại trước khi xóa không
        Doctor doctor = doctorDAO.findById(id);
        if (doctor != null) {
            try {
                doctorDAO.delete(id);
            } catch (Exception e) {
                // Xử lý lỗi nếu bác sĩ đã có lịch hẹn/hồ sơ bệnh án (ràng buộc khóa ngoại)
                throw new RuntimeException("Không thể xóa bác sĩ vì đã có dữ liệu liên quan (lịch hẹn/hồ sơ).");
            }
        }
    }
}