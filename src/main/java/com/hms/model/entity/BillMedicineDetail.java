package com.hms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entity đại diện cho bảng "bill_medicine_details".
 * Lưu trữ chi tiết nội dung của một Hóa đơn (mỗi dòng tương ứng một loại thuốc được mua).
 */
@Setter
@Getter
@Entity
@Table(name = "bill_medicine_details")
public class BillMedicineDetail {

    /** Khóa chính của dòng chi tiết. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * Liên kết n-1 về Hóa đơn gốc. 
     * Một hóa đơn có nhiều dòng chi tiết.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    /** 
     * Loại thuốc được mua. 
     * Liên kết n-1 về bảng danh mục Thuốc.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    /** Số lượng thuốc bán ra. */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /** Đơn giá tại thời điểm xuất hóa đơn (phòng trường hợp sau này giá thuốc thay đổi). */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

}
