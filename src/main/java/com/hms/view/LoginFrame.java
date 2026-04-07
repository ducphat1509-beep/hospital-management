package com.hms.view;

import com.hms.model.dto.UserSessionDTO;
import com.hms.model.entity.Account;
import com.hms.model.enumtype.UserRole;
import com.hms.service.AuthService;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private AuthService authService;

    public LoginFrame(AuthService authService) {
        this.authService = authService;
        initUI();
    }

    private void initUI() {
        setTitle("Đăng nhập hệ thống");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; txtUsername = new JTextField(15); add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; txtPassword = new JPasswordField(15); add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnLogin = new JButton("Login");
        JButton btnSignUp = new JButton("Sign Up");

        btnPanel.add(btnLogin);
        btnPanel.add(btnSignUp);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        btnLogin.addActionListener(e -> handleLogin());
        btnSignUp.addActionListener(e -> showSignUpDialog());
    }

    private void showSignUpDialog() {
        JDialog dialog = new JDialog(this, "Đăng ký tài khoản", true);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JComboBox<UserRole> roleCombo = new JComboBox<>(UserRole.values());

        dialog.add(new JLabel(" Username:"));
        dialog.add(userField);
        dialog.add(new JLabel(" Password:"));
        dialog.add(passField);
        dialog.add(new JLabel(" Role:"));
        dialog.add(roleCombo);

        JButton submit = new JButton("Register");
        submit.addActionListener(e -> {
            try {
                Account newAcc = new Account();
                newAcc.setUsername(userField.getText());
                newAcc.setRole((UserRole) roleCombo.getSelectedItem());

                authService.register(newAcc, new String(passField.getPassword()));

                JOptionPane.showMessageDialog(dialog, "Đăng ký thành công!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage());
            }
        });

        dialog.add(new JLabel(""));
        dialog.add(submit);
        dialog.setVisible(true);
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        try {
            // 1. Lấy Entity từ Database
            Account account = authService.login(username, password);

            // 2. Chuyển đổi Entity sang DTO (UserSessionDTO) để dùng cho toàn hệ thống
            UserSessionDTO session = new UserSessionDTO();
            session.setAccountId(account.getId());
            session.setUsername(account.getUsername());
            session.setRole(account.getRole());

            // Xử lý Display Name và Reference ID dựa trên Role
            if (account.getRole() == UserRole.DOCTOR && account.getDoctor() != null) {
                session.setDisplayName("BS. " + account.getDoctor().getFullName());
                session.setReferenceId(account.getDoctor().getId());

            } else if (account.getRole() == UserRole.PATIENT && account.getPatient() != null) {
                session.setDisplayName(account.getPatient().getFullName());
                session.setReferenceId(account.getPatient().getId());

            } else if (account.getRole() == UserRole.RECEPTIONIST) {
                // Lễ tân thường không có bảng riêng, dùng username hoặc một tên mặc định
                session.setDisplayName("Lễ tân: " + account.getUsername());
                session.setReferenceId(account.getId()); // Dùng luôn ID của Account

            } else if (account.getRole() == UserRole.ADMIN) {
                session.setDisplayName("Quản trị viên (" + account.getUsername() + ")");
                session.setReferenceId(account.getId());

            } else {
                session.setDisplayName(account.getUsername());
            }

            JOptionPane.showMessageDialog(this, "Chào mừng " + session.getDisplayName());

            // 3. Mở MainFrame và truyền DTO vào thay vì truyền Entity
            new MainFrame(session).setVisible(true);
            this.dispose();

        } catch (Exception ex) {
            ex.printStackTrace(); // In ra console để debug nếu có lỗi logic
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}