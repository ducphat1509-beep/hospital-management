package com.hms.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Lớp tiện ích (Utility class) để quản lý kết nối JPA tới cơ sở dữ liệu.
 * Cung cấp các phương thức để khởi tạo và đóng EntityManagerFactory.
 */
public class JpaUtil {
    /**
     * EntityManagerFactory là "Nhà máy" tạo ra các kết nối (Session) tới cơ sở dữ liệu.
     * Do việc khởi tạo rất nặng và tốn tài nguyên nên chỉ khởi tạo 1 lần duy nhất (Singleton pattern) 
     * cho toàn bộ ứng dụng sử dụng cấu hình "HospitalPU" từ persistence.xml.
     */
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalPU");

    /**
     * Lấy một phiên bản EntityManager để thực hiện các thao tác với Database (Thêm, sửa, xóa, truy vấn).
     * @return một đối tượng EntityManager mới. Quá trình làm việc cần được bao bọc trong Transaction.
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Đóng EntityManagerFactory, giải phóng các kết nối tới database.
     * Phương thức này nên được gọi khi tắt ứng dụng để tránh rò rỉ tài nguyên.
     */
    public static void shutDown() {
        if (emf != null) emf.close();
    }
}