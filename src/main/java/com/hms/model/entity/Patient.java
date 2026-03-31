package com.hms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
public class Patient {

    // --- Getter và Setter (Bạn dùng Alt+Insert trong IntelliJ để sinh tự động nhé) ---
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    @Setter
    @Getter
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Override
    public String toString() {
        return this.fullName; // Chỉ hiện tên trên Dropdown
    }

    @Setter
    @Getter
    @Column(name = "dob")
    private LocalDate dob; // Dùng LocalDate cho ngày sinh (chỉ có ngày/tháng/năm)

    @Setter
    @Getter
    @Enumerated(EnumType.STRING) // (1) Chỉ định Hibernate lưu giá trị Enum dưới dạng String ('MALE', 'FEMALE'...)
    @Column(name = "gender")
    private Gender gender;

    @Setter
    @Getter
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- Enum nội bộ để khớp với SQL ---
    public enum Gender {
        MALE, FEMALE, OTHER
    }

}