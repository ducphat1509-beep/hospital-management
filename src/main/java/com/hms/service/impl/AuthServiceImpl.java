package com.hms.service.impl;

import com.hms.dao.AccountDAO;
import com.hms.model.entity.Account;
import com.hms.service.AuthService;
import com.hms.config.PasswordUtil;

public class AuthServiceImpl implements AuthService {
    private final AccountDAO accountDAO;

    public AuthServiceImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public void register(Account account, String rawPassword) {
        // Hash mật khẩu trước khi lưu
        account.setPasswordHash(PasswordUtil.hash(rawPassword));
        accountDAO.save(account);
    }

    @Override
    public Account login(String username, String password) {
        Account account = accountDAO.findByUsername(username);
        if (account != null && PasswordUtil.verify(password, account.getPasswordHash())) {
            return account;
        }
        throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không đúng!");
    }
}