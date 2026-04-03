package com.hms.controller;

import com.hms.model.entity.BillMedicineDetail;
import com.hms.service.BillMedicineDetailService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class BillMedicineDetailController {

    private final BillMedicineDetailService billMedicineDetailService;

    public List<BillMedicineDetail> getAllDetails() {
        return billMedicineDetailService.getAllDetails();
    }

    public BillMedicineDetail getDetailById(Long id) {
        return billMedicineDetailService.getDetailById(id);
    }

    public BillMedicineDetail addMedicine(Long billId, Long medicineId, int quantity) {
        return billMedicineDetailService.addMedicineToBill(billId, medicineId, quantity);
    }

    public BigDecimal computeSubtotal(BillMedicineDetail detail) {
        return billMedicineDetailService.computeSubtotal(detail);
    }

    public void deleteDetail(Long id) {
        billMedicineDetailService.deleteDetail(id);
    }
}

