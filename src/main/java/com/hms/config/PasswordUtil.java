package com.hms.config;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // Băm mật khẩu với salt mặc định
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Kiểm tra mật khẩu thô với bản băm trong DB
    public static boolean verify(String raw, String hash) {
        try {
            return BCrypt.checkpw(raw, hash);
        } catch (Exception e) {
            return false;
        }
    }
}