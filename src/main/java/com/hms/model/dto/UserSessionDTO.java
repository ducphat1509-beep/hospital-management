package com.hms.model.dto;

import com.hms.model.enumtype.UserRole;
import lombok.Data;

/**
 * DTO (Data Transfer Object) đại diện cho phiên đăng nhập (Session) của người dùng hiện tại.
 * Lưu trữ các thông tin thiết yếu sau khi người dùng xác thực thành công.
 * Dữ liệu này được truyền qua lại giữa các màn hình (MainFrame, các Panel).
 */
@Data
public class UserSessionDTO {
    /** 
     * Khóa chính của bảng Account (Tài khoản đăng nhập). 
     */
    private Long accountId;
    
    /** 
     * Tên đăng nhập (username). 
     */
    private String username;
    
    /** 
     * Vai trò quyền hạn của người dùng trong hệ thống (ADMIN, DOCTOR, RECEPTIONIST, PATIENT). 
     */
    private UserRole role;
    
    /** 
     * Tên đầy đủ thực tế của người dùng (tên thật của Bác sĩ hoặc Bệnh nhân tương ứng). 
     */
    private String displayName; 
    
    /** 
     * ID tham chiếu sang Entity tương ứng (ID của Doctor nếu role=DOCTOR, ID của Patient nếu role=PATIENT, ...).
     * Dùng để phục vụ việc truy xuất dữ liệu cá nhân dựa trên tài khoản đang đăng nhập. 
     */
    private Long referenceId;   
}