package com.hms.controller;

import com.hms.model.entity.Medicine;
import com.hms.service.MedicineService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    public List<Medicine> getAllMedicines() {
        return medicineService.getAllMedicines();
    }

    public Medicine getMedicineById(Long id) {
        return medicineService.getMedicineById(id);
    }

    public void addMedicine(Medicine medicine) {
        medicineService.createMedicine(medicine);
    }

    public void updateMedicine(Medicine medicine) {
        medicineService.updateMedicine(medicine);
    }

    public void deleteMedicine(Long id) {
        medicineService.deleteMedicine(id);
    }

    public void increaseStock(Long medicineId, int amount) {
        medicineService.increaseStock(medicineId, amount);
    }

    public void decreaseStock(Long medicineId, int amount) {
        medicineService.decreaseStock(medicineId, amount);
    }
}
