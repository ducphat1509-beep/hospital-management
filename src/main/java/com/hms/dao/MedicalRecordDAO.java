package com.hms.dao;

import com.hms.model.entity.MedicalRecord;
import java.util.List;

/**
 * Interface định nghĩa các phương thức Data Access Object (DAO) cho MedicalRecord (Hồ sơ bệnh án).
 */
public interface MedicalRecordDAO {
    /** Lấy danh mục toàn bộ hồ sơ bệnh án. */
    List<MedicalRecord> findAll();
    
    /** Tìm hồ sơ bệnh án theo ID. */
    MedicalRecord findById(Long id);
    
    /** Tìm hồ sơ bệnh án theo ID của Cuộc hẹn (Appointment) tương ứng. */
    MedicalRecord findByAppointmentId(Long appointmentId);
    
    /** Lưu mới hoặc cập nhật hồ sơ bệnh án. */
    void save(MedicalRecord medicalRecord);
    
    /** Xóa hồ sơ bệnh án. */
    void delete(Long id);
}
