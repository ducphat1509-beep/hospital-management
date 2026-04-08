package com.hms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity đại diện cho bảng "prescriptions" trong cơ sở dữ liệu.
 * Lưu trữ thông tin một Đơn thuốc chung, liên kết với một Hồ sơ khám (MedicalRecord).
 */
@Setter
@Getter
@Entity
@Table(name = "prescriptions")
public class Prescription {

    /** Khóa chính của Đơn thuốc. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long id;

    /** 
     * Liên kết 1-1 tới Hồ sơ y tế (MedicalRecord).
     * Một hồ sơ khám thì chỉ có 1 đơn thuốc duy nhất. 
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false, unique = true)
    private MedicalRecord medicalRecord;

    /** Biên niên tự động: Ngày giờ kê đơn. */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Biên niên tự động: Ngày giờ sửa đổi đơn thuốc (nếu có). */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** 
     * Danh sách các chi tiết đơn thuốc (PrescriptionDetail).
     * Mỗi một dòng chi tiết chỉ định Tên thuốc, liều lượng, số lượng dùng. 
     */
    @OneToMany(mappedBy = "prescription", fetch = FetchType.LAZY)
    private List<PrescriptionDetail> details = new ArrayList<>();

    /** Callback của JPA, cập nhật Audit date. */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    /** Callback của JPA, cập nhật lại Audit date khi modify. */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
