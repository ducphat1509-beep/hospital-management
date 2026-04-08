package com.hms.dao;

import com.hms.model.entity.Appointment;
import java.util.List;

/**
 * Interface định nghĩa các thao tác (CRUD) đối với bảng "appointments".
 */
public interface AppointmentDAO {
    
    /**
     * Lấy danh sách toàn bộ các cuộc hẹn có trong hệ thống.
     * @return Danh sách các đối tượng Appointment.
     */
    List<Appointment> findAll();
    
    /**
     * Tìm kiếm cuộc hẹn theo khóa chính.
     * @param id ID của cuộc hẹn.
     * @return Đối tượng Appointment nếu tìm thấy, ngược lại trả về null.
     */
    Appointment findById(Long id);
    
    /**
     * Lưu mới hoặc cập nhật thông tin một cuộc hẹn.
     * @param appointment Đối tượng cần lưu.
     */
    void save(Appointment appointment);
    
    /**
     * Xóa một cuộc hẹn khỏi hệ thống dựa trên ID.
     * @param id Khóa chính của cuộc hẹn cần xóa.
     */
    void delete(Long id);
}