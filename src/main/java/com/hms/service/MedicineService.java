package com.hms.service;

import com.hms.model.entity.Medicine;
import java.util.List;

public interface MedicineService {

    List<Medicine> getAllMedicines();

    Medicine getMedicineById(Long id);

    void createMedicine(Medicine medicine);

    void updateMedicine(Medicine medicine);

    void deleteMedicine(Long id);

    /** Adjust stock by delta (positive = restock, negative = issue). Resulting stock must not be negative. */
    void adjustStock(Long medicineId, int delta);

    void increaseStock(Long medicineId, int amount);

    void decreaseStock(Long medicineId, int amount);
}
