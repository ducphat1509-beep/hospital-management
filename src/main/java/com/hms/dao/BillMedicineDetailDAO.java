package com.hms.dao;

import com.hms.model.entity.BillMedicineDetail;
import java.util.List;

/**
 * Interface định nghĩa các phương thức Data Access Object (DAO) cho BillMedicineDetail (Chi tiết hóa đơn).
 */
public interface BillMedicineDetailDAO {
    /** Lấy toàn bộ chi tiết hóa đơn trong DB. */
    List<BillMedicineDetail> findAll();
    
    /** Tìm kiếm chi tiết hóa đơn theo ID. */
    BillMedicineDetail findById(Long id);
    
    /** Tìm toàn bộ các chi tiết thuộc về một Hóa đơn cụ thể. */
    List<BillMedicineDetail> findByBillId(Long billId);
    
    /** Kiểm tra xem một Hóa đơn đã tồn tại loại Thuốc này chưa. */
    BillMedicineDetail findByBillAndMedicine(Long billId, Long medicineId);
    
    /** Lưu mới hoặc cập nhật một dòng chi tiết hóa đơn. */
    void save(BillMedicineDetail detail);
    
    /** Xóa một dòng chi tiết hóa đơn. */
    void delete(Long id);
}
