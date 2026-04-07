package com.hms.dao;
import com.hms.model.entity.Account;

public interface AccountDAO {
    Account findByUsername(String username);
    void save(Account account);
}