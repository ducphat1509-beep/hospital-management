package com.hms.dao;

import com.hms.model.entity.PrescriptionDetail;
import java.util.List;

public interface PrescriptionDetailDAO {
    List<PrescriptionDetail> findAll();
    PrescriptionDetail findById(Long id);
    PrescriptionDetail findByPrescriptionAndMedicine(Long prescriptionId, Long medicineId);
    void save(PrescriptionDetail detail);
    void delete(Long id);
}
