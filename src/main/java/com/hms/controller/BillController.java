package com.hms.controller;

import com.hms.model.entity.Bill;
import com.hms.service.BillService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    public Bill getBillById(Long id) {
        return billService.getBillById(id);
    }

    public Bill createBill(Long patientId) {
        return billService.createBill(patientId);
    }

    public void saveBill(Bill bill) {
        billService.saveBill(bill);
    }

    public void deleteBill(Long id) {
        billService.deleteBill(id);
    }

    public BigDecimal calculateTotalAmount(Long billId) {
        return billService.calculateTotalAmount(billId);
    }
}
