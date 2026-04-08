package com.hms.dao;

import com.hms.model.entity.Doctor;
import java.util.List;

/**
 * Interface định nghĩa các phương thức Data Access Object (DAO) cho Doctor (Bác sĩ).
 */
public interface DoctorDAO {
    /** Lấy danh sách toàn bộ bác sĩ có trong hệ thống. */
    List<Doctor> findAll(); 
    
    /** Tìm kiếm bác sĩ theo ID. */
    Doctor findById(Long id); 
    
    /** Lưu mới hoặc cập nhật thông tin bác sĩ. */
    void save(Doctor doctor); 
    
    /** Xóa bác sĩ theo ID. */
    void delete(Long id);     
}