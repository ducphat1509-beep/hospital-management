package com.hms.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // (1) Khai báo với Hibernate: Class này là một thực thể CSDL
@Table(name = "departments") // (2) Ánh xạ chính xác với tên bảng trong MySQL
public class Department {

    @Id // (3) Đánh dấu đây là khóa chính (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (4) Khai báo khóa chính tự tăng (Auto-increment)
    @Column(name = "department_id") // (5) Tên cột trong SQL là department_id
    private Long id;

    @Column(name = "name", nullable = false) // (6) Cột name, không được phép để trống (not null)
    private String name;
    @Override
    public String toString() {
        return this.name;
    }

    @Column(name = "created_at", updatable = false) // (7) Cột ngày tạo, không cho phép cập nhật sau khi đã tạo
    private LocalDateTime createdAt;

    // --- Getter và Setter ---
    // Vì các biến trên là private (để bảo mật), ta cần các hàm public để lấy và gán giá trị

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}