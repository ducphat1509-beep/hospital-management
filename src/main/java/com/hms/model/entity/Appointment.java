package com.hms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity đại diện cho bảng "appointments" trong cơ sở dữ liệu.
 * Lưu trữ thông tin một cuộc hẹn khám bệnh giữa Bệnh nhân và Bác sĩ.
 */
@Setter
@Getter
@Entity
@Table(name = "appointments")
public class Appointment {

    /** Khóa chính tự tăng (Auto-increment). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    /** 
     * Liên kết n-1 tới Patient: Một bệnh nhân có thể có nhiều lịch hẹn.
     * Sử dụng LAZY fetch để tối ưu hiệu suất, chỉ query khi cần thiết. 
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /** 
     * Liên kết n-1 tới Doctor: Một bác sĩ có thể có nhận nhiều lịch hẹn khám.
     * Cột "doctor_id" bắt buộc có dữ liệu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    /** Thời gian dự kiến diễn ra cuộc hẹn. Không được để trống. */
    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    /** Trạng thái của cuộc hẹn. Mặc định khởi tạo là PENDING (Chờ xác nhận/Khám). */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status = AppointmentStatus.PENDING;

    /**
     * Enum nội bộ định nghĩa tập trạng thái của lịch hẹn.
     */
    public enum AppointmentStatus {
        /** Đang chờ xử lý / Chờ khám */
        PENDING, 
        /** Đã xác nhận (Ví dụ bởi lễ tân) */
        CONFIRMED, 
        /** Hoàn tất khám, kết thúc */
        DONE, 
        /** Hủy bỏ */
        CANCELLED
    }

    /** 
     * Liên kết 1-1 tới MedicalRecord (Hồ sơ y tế/ Bệnh án).
     * Một cuộc hẹn sau khi khám xong sẽ sinh ra một hồ sơ bệnh án tương ứng.
     */
    @OneToOne(mappedBy = "appointment", fetch = FetchType.LAZY)
    private MedicalRecord medicalRecord;

}