package com.hms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity đại diện cho bảng "patients" trong cơ sở dữ liệu.
 * Lưu trữ thông tin cá nhân của bệnh nhân tham gia vào hệ thống quản lý khám chữa bệnh.
 */
@Entity
@Table(name = "patients")
public class Patient {

    /** Khóa chính tự tăng. */
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    /** Họ và tên đầy đủ của bệnh nhân. Không được để trống. */
    @Setter
    @Getter
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /**
     * Override lại toString() để khi hiển thị đối tượng Patient
     * lên giao diện UI (ví dụ ComboBox) sẽ chỉ in ra fullName.
     */
    @Override
    public String toString() {
        return this.fullName; // Chỉ hiện tên trên Dropdown UI
    }

    /** Ngày tháng năm sinh của bệnh nhân. */
    @Setter
    @Getter
    @Column(name = "dob")
    private LocalDate dob; // Dùng LocalDate cho ngày sinh (chỉ chứa yyyy-MM-dd)

    /** Giới tính của bệnh nhân, ánh xạ vào Database dưới dạng chuỗi (MALE/FEMALE). */
    @Setter
    @Getter
    @Enumerated(EnumType.STRING) // Chỉ định Hibernate lưu giá trị Enum dưới dạng chuỗi varchar
    @Column(name = "gender")
    private Gender gender;

    /** Số điện thoại liên hệ của bệnh nhân. */
    @Setter
    @Getter
    @Column(name = "phone", length = 20)
    private String phone;

    /** Ngày giờ hồ sơ bệnh nhân được tạo. */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Ngày giờ thông tin bệnh nhân được chỉnh sửa gần nhất. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** 
     * Danh sách các hóa đơn (Bill) liên quan đến bệnh nhân này. 
     * Quan hệ 1-n. 
     */
    @Setter
    @Getter
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Bill> bills = new ArrayList<>();

    /**
     * Lifecycle callback của JPA.
     * Tự động khởi tạo thời gian createdAt và updatedAt khi bản ghi mới được tạo.
     */
    @PrePersist // Chạy ngay trước khi lưu mới vào DB
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Enum nội bộ định nghĩa tập giá trị giới tính.
     */
    public enum Gender {
        MALE, FEMALE, OTHER
    }

}