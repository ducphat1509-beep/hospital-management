package com.hms.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO (Data Transfer Object) chứa thông tin tóm tắt của một cuộc hẹn (Appointment).
 * Được sử dụng phổ biến trên giao diện danh sách cuộc hẹn (Dashboard, AppointmentPanel) 
 * để tối ưu dữ liệu truyền tải thay vì phải lấy toàn bộ Entity cồng kềnh.
 */
@Data
@AllArgsConstructor
public class AppointmentSummaryDTO {
    /** 
     * ID mã cuộc hẹn. 
     */
    private Long id;
    
    /** 
     * Tên đầy đủ của bệnh nhân. 
     */
    private String patientName;
    
    /** 
     * Tên đầy đủ của bác sĩ phụ trách. 
     */
    private String doctorName;
    
    /** 
     * Thời gian (ngày/giờ) diễn ra cuộc hẹn. 
     */
    private LocalDateTime appointmentTime;
    
    /** 
     * Trạng thái hiện tại của cuộc hẹn (vd: PENDING, DONE, CANCELED). 
     */
    private String status;
}