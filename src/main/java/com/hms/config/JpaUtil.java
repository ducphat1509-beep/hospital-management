package com.hms.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    // EntityManagerFactory là "Nhà máy" tạo ra các kết nối, nó rất nặng nên chỉ tạo 1 lần duy nhất
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalPU");

    // Mỗi khi cần làm việc với DB (Thêm, sửa, xóa), ta gọi hàm này để lấy 1 "Công nhân" (EntityManager)
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Đóng nhà máy khi tắt ứng dụng
    public static void shutDown() {
        if (emf != null) emf.close();
    }
}