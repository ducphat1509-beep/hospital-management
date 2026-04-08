package com.hms.dao;

import com.hms.model.entity.PrescriptionDetail;
import java.util.List;

/**
 * Interface định nghĩa các phương thức Data Access Object (DAO) cho PrescriptionDetail (Chi tiết đơn thuốc).
 */
public interface PrescriptionDetailDAO {
    /** Lấy toàn bộ chi tiết đơn thuốc. */
    List<PrescriptionDetail> findAll();
    
    /** Tìm kiếm chi tiết đơn thuốc dựa theo ID. */
    PrescriptionDetail findById(Long id);
    
    /** Kiểm tra xem một đơn thuốc đã được kê loại thuốc này chưa. */
    PrescriptionDetail findByPrescriptionAndMedicine(Long prescriptionId, Long medicineId);
    
    /** Lưu mới/Cập nhật số lượng, liều dùng của một dòng thuốc trong đơn. */
    void save(PrescriptionDetail detail);
    
    /** Xóa một dòng thuốc khỏi đơn thuốc. */
    void delete(Long id);
}
