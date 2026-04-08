package com.hms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity đại diện cho bảng "prescription_details".
 * Lưu trữ chi tiết một loại thuốc được kê trong đơn thuốc.
 */
@Setter
@Getter
@Entity
@Table(name = "prescription_details")
public class PrescriptionDetail {

    /** Khóa chính dòng chi tiết đơn thuốc. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Phiếu kê đơn gốc. 
     * Nhiều chi tiết sẽ thuộc về 1 đơn thuốc duy nhất.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    /**
     * Loại thuốc được kê.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    /** Số lượng thuốc bác sĩ chỉ định. */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /** Liều dùng và hướng dẫn sử dụng (Ví dụ: 1 viên/ngày, uống sau ăn). */
    @Column(name = "dosage")
    private String dosage;

}
