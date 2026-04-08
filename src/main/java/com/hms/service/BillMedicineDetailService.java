package com.hms.service;

import com.hms.model.entity.BillMedicineDetail;
import java.math.BigDecimal;
import java.util.List;

public interface BillMedicineDetailService {

    List<BillMedicineDetail> getAllDetails();

    BillMedicineDetail getDetailById(Long id);

    /**
     * Unit price is taken from the medicine at sale time. Stock is reduced by {@code quantity}.
     * If the same medicine already exists on the bill, quantities are merged.
     */
    BillMedicineDetail addMedicineToBill(Long billId, Long medicineId, int quantity);

    /** quantity × unit price (per line). */
    BigDecimal computeSubtotal(BillMedicineDetail detail);

    void deleteDetail(Long id);

    void removeMedicineFromBill(Long billId, Long medicineId, int quantityToRemove);
}
