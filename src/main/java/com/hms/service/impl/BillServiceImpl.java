package com.hms.service.impl;

import com.hms.dao.BillDAO;
import com.hms.dao.BillMedicineDetailDAO;
import com.hms.dao.PatientDAO;
import com.hms.model.entity.Bill;
import com.hms.model.entity.BillMedicineDetail;
import com.hms.model.entity.Patient;
import com.hms.service.BillService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BillServiceImpl implements BillService {

    private final BillDAO billDAO;
    private final BillMedicineDetailDAO billMedicineDetailDAO;
    private final PatientDAO patientDAO;

    public BillServiceImpl(BillDAO billDAO, BillMedicineDetailDAO billMedicineDetailDAO, PatientDAO patientDAO) {
        this.billDAO = billDAO;
        this.billMedicineDetailDAO = billMedicineDetailDAO;
        this.patientDAO = patientDAO;
    }

    @Override
    public List<Bill> getAllBills() {
        return billDAO.findAll();
    }

    @Override
    public Bill getBillById(Long id) {
        Bill bill = billDAO.findById(id);
        if (bill == null) {
            throw new RuntimeException("Không tìm thấy hóa đơn với ID: " + id);
        }
        return bill;
    }

    @Override
    public Bill createBill(Long patientId) {
        if (patientId == null) {
            throw new RuntimeException("Mã bệnh nhân không được để trống!");
        }
        Patient patient = patientDAO.findById(patientId);
        if (patient == null) {
            throw new RuntimeException("Bệnh nhân không tồn tại!");
        }
        Bill bill = new Bill();
        bill.setPatient(patient);
        bill.setStatus(Bill.BillStatus.UNPAID);
        bill.setTotalAmount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        billDAO.save(bill);
        return bill;
    }

    @Override
    public void saveBill(Bill bill) {
        if (bill == null) {
            throw new RuntimeException("Hóa đơn không hợp lệ!");
        }
        if (bill.getId() != null && billDAO.findById(bill.getId()) == null) {
            throw new RuntimeException("Hóa đơn không tồn tại!");
        }
        billDAO.save(bill);
    }

    @Override
    public void deleteBill(Long id) {
        if (billDAO.findById(id) == null) {
            throw new RuntimeException("Không tìm thấy hóa đơn để xóa!");
        }
        billDAO.delete(id);
    }

    @Override
    public BigDecimal calculateTotalAmount(Long billId) {
        Bill bill = getBillById(billId);
        List<BillMedicineDetail> lines = billMedicineDetailDAO.findByBillId(billId);
        BigDecimal total = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        for (BillMedicineDetail line : lines) {
            BigDecimal unit = line.getPrice() != null ? line.getPrice() : BigDecimal.ZERO;
            int qty = line.getQuantity() != null ? line.getQuantity() : 0;
            BigDecimal lineTotal = unit.multiply(BigDecimal.valueOf(qty)).setScale(2, RoundingMode.HALF_UP);
            total = total.add(lineTotal);
        }
        bill.setTotalAmount(total);
        billDAO.save(bill);
        return total;
    }
}
