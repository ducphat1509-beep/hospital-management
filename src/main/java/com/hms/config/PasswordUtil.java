package com.hms.config;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Lớp tiện ích hỗ trợ bảo mật mật khẩu người dùng.
 * Sử dụng thư viện BCrypt để băm (hash) và xác thực (verify) mật khẩu.
 */
public class PasswordUtil {
    
    /**
     * Băm mật khẩu người dùng truyền vào với một salt ngẫu nhiên.
     * @param password mật khẩu thô (plaintext).
     * @return chuỗi mật khẩu đã được băm an toàn theo thuật toán BCrypt.
     */
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Xác thực mật khẩu người dùng nhập vào so với chuỗi băm lưu trong cơ sở dữ liệu.
     * @param raw mật khẩu thô người dùng gửi lên.
     * @param hash chuỗi mật khẩu băm đã lưu trữ trong Database.
     * @return true nếu mật khẩu khớp, false nếu không khớp hoặc gặp lỗi.
     */
    public static boolean verify(String raw, String hash) {
        try {
            return BCrypt.checkpw(raw, hash);
        } catch (Exception e) {
            return false;
        }
    }
}