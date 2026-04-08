package com.hms.dao;

import com.hms.model.entity.Department;
import java.util.List;

/**
 * Interface định nghĩa các phương thức Data Access Object (DAO) cho Department (Khoa / Phòng ban).
 */
public interface DepartmentDAO {
    /** Lấy danh sách toàn bộ phòng ban. */
    List<Department> findAll();
    
    /** Tìm phòng ban theo ID. */
    Department findById(Long id);
    
    /** Lưu mới hoặc cập nhật phòng ban. */
    void save(Department department);
    
    /** Xóa phòng ban theo ID. */
    void delete(Long id);
}
