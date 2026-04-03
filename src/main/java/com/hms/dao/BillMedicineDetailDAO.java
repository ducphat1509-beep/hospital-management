package com.hms.dao;

import com.hms.model.entity.BillMedicineDetail;
import java.util.List;

public interface BillMedicineDetailDAO {
    List<BillMedicineDetail> findAll();
    BillMedicineDetail findById(Long id);
    List<BillMedicineDetail> findByBillId(Long billId);
    BillMedicineDetail findByBillAndMedicine(Long billId, Long medicineId);
    void save(BillMedicineDetail detail);
    void delete(Long id);
}
