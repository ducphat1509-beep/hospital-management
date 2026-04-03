package com.hms.service;

import com.hms.model.entity.Bill;
import java.math.BigDecimal;
import java.util.List;

public interface BillService {

    List<Bill> getAllBills();

    Bill getBillById(Long id);

    Bill createBill(Long patientId);

    void saveBill(Bill bill);

    void deleteBill(Long id);

    /**
     * Sums line subtotals (quantity × unit price) for all medicine lines and updates the bill.
     */
    BigDecimal calculateTotalAmount(Long billId);
}
