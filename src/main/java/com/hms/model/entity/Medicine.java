package com.hms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity đại diện cho bảng "medicines" (danh mục thuốc/vật tư y tế) trong cơ sở dữ liệu.
 */
@Setter
@Getter
@Entity
@Table(name = "medicines")
public class Medicine {

    /** Khóa chính tự tăng của loại thuốc/vật tư. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id")
    private Long id;

    /** Tên thuốc (ví dụ: Paracetamol 500mg). Không được để trống. */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Override lại toString() hiển thị tên thuốc. 
     * Dùng cho các UI ComboBox/List.
     */
    @Override
    public String toString() {
        return this.name;
    }

    /** 
     * Đơn giá bán của loại thuốc (precision = 10, scale = 2). 
     * Không được để trống. 
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** Số lượng tồn kho hiện tại. Mặc định khởi tạo là 0. */
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    /** Thời điểm loại thuốc được thêm mới vào hệ thống. */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Thời điểm thông tin thuốc (đơn giá, số lượng,...) được cập nhật. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Danh sách các chi tiết Đơn thuốc (PrescriptionDetail) có sử dụng mã thuốc này. */
    @OneToMany(mappedBy = "medicine", fetch = FetchType.LAZY)
    private List<PrescriptionDetail> prescriptionDetails = new ArrayList<>();

    /** Danh sách chi tiết các Hóa đơn (BillMedicineDetail) có bán loại thuốc này. */
    @OneToMany(mappedBy = "medicine", fetch = FetchType.LAZY)
    private List<BillMedicineDetail> billMedicineDetails = new ArrayList<>();

    /**
     * JPA Lifecycle callback: gọi ngay trước khi thực thể được lưu lần đầu.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    /**
     * JPA Lifecycle callback: gọi ngay trước khi thực thể được cập nhật (update).
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
