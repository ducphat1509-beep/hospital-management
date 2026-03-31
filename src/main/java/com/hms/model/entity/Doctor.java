package com.hms.model.entity;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Override
    public String toString() {
        return this.fullName; // Chỉ hiện tên trên Dropdown
    }

    // (1) ĐỊNH NGHĨA MỐI QUAN HỆ: Nhiều bác sĩ - Một khoa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id") // Tên cột khóa ngoại trong bảng SQL
    private Department department;

}