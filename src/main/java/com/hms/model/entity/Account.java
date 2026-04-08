package com.hms.model.entity;

import com.hms.model.enumtype.UserRole;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity đại diện cho bảng "accounts" trong cơ sở dữ liệu.
 * Dùng để lưu trữ thông tin đăng nhập và phân quyền của người dùng hệ thống.
 */
@Entity
@Table(name = "accounts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    /** Khóa chính tự tăng (Auto-increment). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    /** Tên đăng nhập. Đảm bảo duy nhất (unique) và không được để trống. */
    @Column(unique = true, nullable = false)
    private String username;

    /** Mật khẩu đã được băm (hash) bằng BCrypt để bảo mật. Không lưu mật khẩu thô. */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /** Vai trò phân quyền của tài khoản (VD: ADMIN, DOCTOR, PATIENT, RECEPTIONIST). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /** 
     * Liên kết 1-1 tới thực thể Doctor (Bác sĩ). 
     * Chỉ có giá trị khi tài khoản này thuộc về một Bác sĩ (role = DOCTOR).
     */
    @OneToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    /** 
     * Liên kết 1-1 tới thực thể Patient (Bệnh nhân). 
     * Chỉ có giá trị khi tài khoản này thuộc về Bệnh nhân (role = PATIENT).
     */
    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    /** Thời điểm tài khoản được tạo mới trong hệ thống. Chỉ set 1 lần lúc insert. */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** 
     * Lifecycle callback của JPA.
     * Tự động gán thời gian hiện tại vào trường createdAt trước khi record được lưu vào DB.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}