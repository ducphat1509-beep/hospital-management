package com.hms.dao;

import com.hms.model.entity.Medicine;
import java.util.List;

/**
 * Interface định nghĩa các phương thức Data Access Object (DAO) cho Medicine (Danh mục thuốc).
 */
public interface MedicineDAO {
    /** Lấy danh sách toàn bộ thuốc/vật tư y tế trong kho. */
    List<Medicine> findAll();
    
    /** Tìm kiếm thuốc theo ID. */
    Medicine findById(Long id);
    
    /** Lưu mới hoặc cập nhật loại thuốc (vd: thay đổi đơn giá, số lượng). */
    void save(Medicine medicine);
    
    /** Xóa loại thuốc. (Lưu ý: Thường không xóa nếu đã có trong hóa đơn/đơn thuốc). */
    void delete(Long id);
}
