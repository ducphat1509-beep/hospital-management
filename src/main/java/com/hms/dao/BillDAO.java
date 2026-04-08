package com.hms.dao;

import com.hms.model.entity.Bill;
import java.util.List;

/**
 * Interface định nghĩa các phương thức Data Access Object (DAO) cho Bill (Hóa đơn).
 */
public interface BillDAO {
    /** Lấy danh sách toàn bộ hóa đơn. */
    List<Bill> findAll();
    
    /** Tìm hóa đơn theo ID. */
    Bill findById(Long id);
    
    /** Lưu mới hoặc cập nhật hóa đơn. */
    void save(Bill bill);
    
    /** Xóa hóa đơn theo ID. */
    void delete(Long id);
}
