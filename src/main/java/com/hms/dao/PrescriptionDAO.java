package com.hms.dao;

import com.hms.model.entity.Prescription;
import java.util.List;

/**
 * Interface định nghĩa các phương thức Data Access Object (DAO) cho Prescription (Đơn thuốc).
 */
public interface PrescriptionDAO {
    /** Lấy danh sách toàn bộ đơn thuốc. */
    List<Prescription> findAll();
    
    /** Tìm kiếm đơn thuốc theo ID. */
    Prescription findById(Long id);
    
    /** Tìm kiếm đơn thuốc dựa vào ID của Hồ sơ y tế chứa đơn thuốc đó. */
    Prescription findByMedicalRecordId(Long medicalRecordId);
    
    /** Lưu mới hoặc cập nhật thông tin đơn thuốc. */
    void save(Prescription prescription);
    
    /** Xóa một đơn thuốc khỏi cơ sở dữ liệu. */
    void delete(Long id);
}
