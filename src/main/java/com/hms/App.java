package com.hms;

import com.hms.dao.AccountDAO;
import com.hms.dao.impl.AccountDAOImpl;
import com.hms.service.AuthService;
import com.hms.service.impl.AuthServiceImpl;
import com.hms.view.LoginFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Cấu hình giao diện
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> {
            // Khởi tạo các tầng
            AccountDAO accountDAO = new AccountDAOImpl();
            AuthService authService = new AuthServiceImpl(accountDAO);

            // Hiện màn hình login
            new LoginFrame(authService).setVisible(true);
        });
    }
}