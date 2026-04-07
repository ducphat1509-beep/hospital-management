package com.hms.service;
import com.hms.model.entity.Account;

public interface AuthService {
    Account login(String username, String password);
    void register(Account account, String rawPassword);
}