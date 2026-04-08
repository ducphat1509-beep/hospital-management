package com.hms.dao;

import com.hms.model.entity.Patient;
import java.util.List;

/**
 * Interface định nghĩa các phương thức Data Access Object (DAO) cho Patient (Bệnh nhân).
 */
public interface PatientDAO {
    /** Lấy danh sách tất cả những bệnh nhân có trong hệ thống. */
    List<Patient> findAll();
    
    /** Tìm kiếm bệnh nhân theo ID. */
    Patient findById(Long id);
    
    /** Lưu mới hoặc cập nhật thông tin cá nhân của bệnh nhân. */
    void save(Patient patient);
    
    /** Xóa thông tin bệnh nhân. */
    void delete(Long id);
}