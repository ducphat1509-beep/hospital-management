package com.hms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity đại diện cho bảng "departments" trong cơ sở dữ liệu.
 * Dùng để quản lý danh sách các Khoa/Phòng khám trong bệnh viện (VD: Khoa Nội, Khoa Ngoại).
 */
@Setter
@Getter
@Entity // (1) Khai báo với Hibernate: Class này là một thực thể CSDL
@Table(name = "departments") // (2) Ánh xạ chính xác với tên bảng trong cơ sở dữ liệu
public class Department {

    /** Khóa chính của phòng ban/khoa. Tự động tăng. */
    @Id // (3) Đánh dấu đây là khóa chính (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (4) Khai báo khóa chính tự tăng (Auto-increment)
    @Column(name = "department_id") // (5) Tên cột trong SQL là department_id
    private Long id;

    /** Tên của khoa (ví dụ: Khoa Cấp Cứu). */
    @Column(name = "name", nullable = false) // (6) Cột name, không được phép để trống (not null)
    private String name;

    /** 
     * Override hàm toString để khi hiển thị trong ComboBox (UI) 
     * thì nó sẽ in ra cái tên thay vì tham chiếu hashCode.
     */
    @Override
    public String toString() {
        return this.name;
    }

    /** Thời điểm phòng/khoa này được tạo trong hệ thống. */
    @Column(name = "created_at", updatable = false) // (7) Cột ngày tạo, không cho phép cập nhật sau khi đã tạo
    private LocalDateTime createdAt;

}