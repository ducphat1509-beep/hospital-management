package com.hms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity đại diện cho bảng "bills" trong cơ sở dữ liệu.
 * Lưu trữ thông tin hóa đơn thanh toán của bệnh nhân.
 */
@Setter
@Getter
@Entity
@Table(name = "bills")
public class Bill {

    /** Khóa chính tự tăng. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long id;

    /** 
     * Liên kết n-1 tới Bệnh nhân mà hóa đơn này thuộc về. 
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /** Tổng số tiền của hóa đơn (Ví dụ: bao gồm phí khám, phí thuốc, ...). Mặc định là 0. */
    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /** Trạng thái thanh toán của hóa đơn. Mặc định khi tạo ra là UNPAID. */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BillStatus status = BillStatus.UNPAID;

    /** Thời điểm in đơn / tạo mã thanh toán. */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Thời điểm cập nhật cuối cùng (Ví dụ: khi xác nhận thanh toán). */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** 
     * Danh sách chi tiết các loại thuốc nằm trong hóa đơn này.
     * Liên kết 1-nhiều tới bảng BillMedicineDetail.
     */
    @OneToMany(mappedBy = "bill", fetch = FetchType.LAZY)
    private List<BillMedicineDetail> billMedicineDetails = new ArrayList<>();

    /** Enum các trạng thái có thể của một Hóa đơn. */
    public enum BillStatus {
        /** Chưa thanh toán */
        UNPAID, 
        /** Đã hoàn tất thanh toán */
        PAID, 
        /** Đã hủy bỏ */
        CANCELLED
    }

    /** 
     * Hibernate/JPA callback: Tự động ghi lại thời điểm khởi tạo 
     * khi đối tượng được persist (lưu lần đầu) vào Database. 
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    /** 
     * Hibernate/JPA callback: Tự động cập nhật thời điểm cập nhật mới nhất 
     * trước khi đối tượng bị update vào Database. 
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
