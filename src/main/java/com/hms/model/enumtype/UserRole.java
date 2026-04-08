package com.hms.model.enumtype;

/**
 * Enum định nghĩa các vai trò (Role) phân quyền trong hệ thống Quản lý Bệnh viện.
 */
public enum UserRole {
    /** Quản trị viên: Có toàn quyền trên mọi mặt của hệ thống. */
    ADMIN,          
    
    /** Bác sĩ: Quản lý cuộc hẹn cá nhân, khám bệnh, kê đơn thuốc và xem hồ sơ bệnh nhân do mình phụ trách. */
    DOCTOR,         
    
    /** Lễ tân: Quản lý cuộc hẹn chung, thêm bệnh nhân, xuất thẻ/bệnh án và thanh toán hóa đơn. */
    RECEPTIONIST,   
    
    /** Bệnh nhân: Xem thông tin hồ sơ y tế cá nhân, lịch hẹn và lịch sử đơn thuốc của mình (nếu có tính năng tự phục vụ). */
    PATIENT         
}