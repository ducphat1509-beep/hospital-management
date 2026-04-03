package com.hms.dao;

import com.hms.model.entity.Bill;
import java.util.List;

public interface BillDAO {
    List<Bill> findAll();
    Bill findById(Long id);
    void save(Bill bill);
    void delete(Long id);
}
