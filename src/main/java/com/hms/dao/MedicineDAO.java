package com.hms.dao;

import com.hms.model.entity.Medicine;
import java.util.List;

public interface MedicineDAO {
    List<Medicine> findAll();
    Medicine findById(Long id);
    void save(Medicine medicine);
    void delete(Long id);
}
