package com.hms.dao;
import com.hms.model.entity.Account;

/**
 * Interface định nghĩa các phương thức giao tiếp với bảng "accounts" trong cơ sở dữ liệu.
 */
public interface AccountDAO {
    
    /**
     * Tìm kiếm một tài khoản dựa trên tên đăng nhập (username).
     * @param username Tên đăng nhập cần tìm.
     * @return Đối tượng Account nếu tìm thấy, ngược lại trả về null.
     */
    Account findByUsername(String username);
    
    /**
     * Lưu mới hoặc cập nhật một tài khoản vào cơ sở dữ liệu.
     * @param account Đối tượng Account cần lưu (nếu id = null sẽ tạo mới, ngược lại sẽ update).
     */
    void save(Account account);
}