package com.hms.service.impl;

import com.hms.dao.MedicineDAO;
import com.hms.model.entity.Medicine;
import com.hms.service.MedicineService;

import java.math.BigDecimal;
import java.util.List;

public class MedicineServiceImpl implements MedicineService {

    private final MedicineDAO medicineDAO;

    public MedicineServiceImpl(MedicineDAO medicineDAO) {
        this.medicineDAO = medicineDAO;
    }

    @Override
    public List<Medicine> getAllMedicines() {
        return medicineDAO.findAll();
    }

    @Override
    public Medicine getMedicineById(Long id) {
        Medicine m = medicineDAO.findById(id);
        if (m == null) {
            throw new RuntimeException("Không tìm thấy thuốc với ID: " + id);
        }
        return m;
    }

    @Override
    public void createMedicine(Medicine medicine) {
        if (medicine == null) {
            throw new RuntimeException("Thông tin thuốc không hợp lệ!");
        }
        if (medicine.getName() == null || medicine.getName().trim().isEmpty()) {
            throw new RuntimeException("Tên thuốc không được để trống!");
        }
        if (medicine.getPrice() == null || medicine.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Giá thuốc không hợp lệ!");
        }
        if (medicine.getStockQuantity() != null && medicine.getStockQuantity() < 0) {
            throw new RuntimeException("Tồn kho không được âm!");
        }
        medicineDAO.save(medicine);
    }

    @Override
    public void updateMedicine(Medicine medicine) {
        if (medicine == null || medicine.getId() == null) {
            throw new RuntimeException("Thông tin thuốc không hợp lệ!");
        }
        if (medicineDAO.findById(medicine.getId()) == null) {
            throw new RuntimeException("Thuốc không tồn tại!");
        }
        if (medicine.getName() == null || medicine.getName().trim().isEmpty()) {
            throw new RuntimeException("Tên thuốc không được để trống!");
        }
        if (medicine.getPrice() == null || medicine.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Giá thuốc không hợp lệ!");
        }
        if (medicine.getStockQuantity() != null && medicine.getStockQuantity() < 0) {
            throw new RuntimeException("Tồn kho không được âm!");
        }
        medicineDAO.save(medicine);
    }

    @Override
    public void deleteMedicine(Long id) {
        if (medicineDAO.findById(id) == null) {
            throw new RuntimeException("Không tìm thấy thuốc để xóa!");
        }
        medicineDAO.delete(id);
    }

    @Override
    public void adjustStock(Long medicineId, int delta) {
        Medicine medicine = getMedicineById(medicineId);
        int current = medicine.getStockQuantity() != null ? medicine.getStockQuantity() : 0;
        int next = current + delta;
        if (next < 0) {
            throw new RuntimeException("Tồn kho không đủ (hiện có " + current + ")!");
        }
        medicine.setStockQuantity(next);
        medicineDAO.save(medicine);
    }

    @Override
    public void increaseStock(Long medicineId, int amount) {
        if (amount <= 0) {
            throw new RuntimeException("Số lượng nhập kho phải lớn hơn 0!");
        }
        adjustStock(medicineId, amount);
    }

    @Override
    public void decreaseStock(Long medicineId, int amount) {
        if (amount <= 0) {
            throw new RuntimeException("Số lượng xuất kho phải lớn hơn 0!");
        }
        adjustStock(medicineId, -amount);
    }
}
