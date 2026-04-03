package com.hms.service.impl;

import com.hms.dao.BillDAO;
import com.hms.dao.BillMedicineDetailDAO;
import com.hms.dao.MedicineDAO;
import com.hms.model.entity.Bill;
import com.hms.model.entity.BillMedicineDetail;
import com.hms.model.entity.Medicine;
import com.hms.service.BillMedicineDetailService;
import com.hms.service.BillService;
import com.hms.service.MedicineService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BillMedicineDetailServiceImpl implements BillMedicineDetailService {

    private final BillDAO billDAO;
    private final BillMedicineDetailDAO billMedicineDetailDAO;
    private final MedicineDAO medicineDAO;
    private final MedicineService medicineService;
    private final BillService billService;

    public BillMedicineDetailServiceImpl(
            BillDAO billDAO,
            BillMedicineDetailDAO billMedicineDetailDAO,
            MedicineDAO medicineDAO,
            MedicineService medicineService,
            BillService billService
    ) {
        this.billDAO = billDAO;
        this.billMedicineDetailDAO = billMedicineDetailDAO;
        this.medicineDAO = medicineDAO;
        this.medicineService = medicineService;
        this.billService = billService;
    }

    @Override
    public List<BillMedicineDetail> getAllDetails() {
        return billMedicineDetailDAO.findAll();
    }

    @Override
    public BillMedicineDetail getDetailById(Long id) {
        BillMedicineDetail d = billMedicineDetailDAO.findById(id);
        if (d == null) {
            throw new RuntimeException("Không tìm thấy chi tiết hóa đơn với ID: " + id);
        }
        return d;
    }

    @Override
    public BillMedicineDetail addMedicineToBill(Long billId, Long medicineId, int quantity) {
        if (billId == null || medicineId == null) {
            throw new RuntimeException("Hóa đơn và thuốc không được để trống!");
        }
        if (quantity <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0!");
        }

        Bill bill = billDAO.findById(billId);
        if (bill == null) {
            throw new RuntimeException("Hóa đơn không tồn tại!");
        }
        if (bill.getStatus() != Bill.BillStatus.UNPAID) {
            throw new RuntimeException("Chỉ có thể thêm thuốc vào hóa đơn chưa thanh toán!");
        }

        Medicine medicine = medicineDAO.findById(medicineId);
        if (medicine == null) {
            throw new RuntimeException("Thuốc không tồn tại!");
        }

        BillMedicineDetail existing = billMedicineDetailDAO.findByBillAndMedicine(billId, medicineId);

        int stock = medicine.getStockQuantity() != null ? medicine.getStockQuantity() : 0;
        if (existing != null) {
            int mergedTotal = existing.getQuantity() + quantity;
            if (mergedTotal > stock) {
                throw new RuntimeException("Số lượng vượt quá tồn kho (còn " + stock + ")!");
            }
        } else {
            if (quantity > stock) {
                throw new RuntimeException("Số lượng vượt quá tồn kho (còn " + stock + ")!");
            }
        }

        medicineService.decreaseStock(medicineId, quantity);

        BigDecimal unitPrice = medicine.getPrice() != null
                ? medicine.getPrice().setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        if (existing != null) {
            int newQty = existing.getQuantity() + quantity;
            existing.setQuantity(newQty);
            existing.setPrice(unitPrice);
            billMedicineDetailDAO.save(existing);
            billService.calculateTotalAmount(billId);
            return existing;
        }

        BillMedicineDetail detail = new BillMedicineDetail();
        detail.setBill(bill);
        detail.setMedicine(medicine);
        detail.setQuantity(quantity);
        detail.setPrice(unitPrice);
        billMedicineDetailDAO.save(detail);
        billService.calculateTotalAmount(billId);
        return detail;
    }

    @Override
    public BigDecimal computeSubtotal(BillMedicineDetail detail) {
        if (detail == null) {
            throw new RuntimeException("Chi tiết không hợp lệ!");
        }
        BigDecimal unit = detail.getPrice() != null ? detail.getPrice() : BigDecimal.ZERO;
        int qty = detail.getQuantity() != null ? detail.getQuantity() : 0;
        return unit.multiply(BigDecimal.valueOf(qty)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void deleteDetail(Long id) {
        BillMedicineDetail detail = billMedicineDetailDAO.findById(id);
        if (detail == null) {
            throw new RuntimeException("Không tìm thấy chi tiết để xóa!");
        }
        Long billId = detail.getBill().getId();
        int qty = detail.getQuantity() != null ? detail.getQuantity() : 0;
        Long medId = detail.getMedicine().getId();
        medicineService.increaseStock(medId, qty);
        billMedicineDetailDAO.delete(id);
        billService.calculateTotalAmount(billId);
    }
}
