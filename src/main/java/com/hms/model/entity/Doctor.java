package com.hms.model.entity;

import lombok.*;
import jakarta.persistence.*;

/**
 * Entity đại diện cho bảng "doctors" trong cơ sở dữ liệu.
 * Lưu trữ thông tin cơ bản và chuyên khoa của bác sĩ.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor {

    /** Khóa chính tự tăng của bác sĩ. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long id;

    /** Họ và tên đầy đủ của bác sĩ. Không được để trống. */
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /**
     * Override phương thức toString() để tự động hiển thị tên bác sĩ
     * khi đối tượng này được nạp vào các ComboBox trên giao diện (ví dụ DoctorPanel).
     */
    @Override
    public String toString() {
        return this.fullName; // Chỉ hiện tên trên Dropdown
    }

    /** 
     * Liên kết n-1 tới Department (Khoa). 
     * Một bác sĩ chỉ thuộc về một Khoa, nhưng một Khoa có nhiều bác sĩ.
     * Sử dụng EAGER fetch vì khi load thông tin bác sĩ thường xuyên cần biết bác sĩ đó ở khoa nào.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id") // Tên cột khóa ngoại trong bảng SQL
    private Department department;
}