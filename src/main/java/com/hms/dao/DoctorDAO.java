package com.hms.dao;

import com.hms.model.entity.Doctor;
import java.util.List;

public interface DoctorDAO {
    List<Doctor> findAll(); // Lấy tất cả bác sĩ
    Doctor findById(Long id); // Tìm theo ID
    void save(Doctor doctor); // Lưu bác sĩ mới
    void delete(Long id);     // Xóa bác sĩ
}