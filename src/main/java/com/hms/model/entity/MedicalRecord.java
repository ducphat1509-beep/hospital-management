package com.hms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity đại diện cho bảng "medical_records" trong cơ sở dữ liệu.
 * Lưu trữ hồ sơ bệnh án (kết quả khám, chẩn đoán, ghi chú) của một cuộc hẹn khám.
 */
@Setter
@Getter
@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    /** Khóa chính tự tăng của Hồ sơ bệnh án. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    /** 
     * Liên kết 1-1 tới Appointment (Cuộc hẹn).
     * Mỗi cuộc hẹn khi khám xong sẽ tạo ra duy nhất 1 bản ghi hồ sơ.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    /** Chẩn đoán của bác sĩ sau khi khám. Dùng TEXT để lưu chuỗi dài. */
    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    /** Ghi chú bổ sung hoặc triệu chứng bệnh nhân. */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /** 
     * Thời điểm làm hồ sơ bệnh án.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Thời điểm cập nhật hồ sơ bệnh án gần nhất. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** 
     * Liên kết 1-1 tới Prescription (Đơn thuốc). 
     * Nếu bác sĩ có kê đơn thuốc, thì hồ sơ này sẽ được liên kết với một đơn thuốc.
     */
    @OneToOne(mappedBy = "medicalRecord", fetch = FetchType.LAZY)
    private Prescription prescription;

    /**
     * JPA Lifecycle callback: gọi lúc thực thể được lưu lần đầu.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    /**
     * JPA Lifecycle callback: gọi lúc cập nhật thực thể.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
