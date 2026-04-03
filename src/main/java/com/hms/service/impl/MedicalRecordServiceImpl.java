package com.hms.service.impl;

import com.hms.dao.AppointmentDAO;
import com.hms.dao.MedicalRecordDAO;
import com.hms.model.entity.Appointment;
import com.hms.model.entity.MedicalRecord;
import com.hms.service.MedicalRecordService;

import java.util.List;

public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordDAO medicalRecordDAO;
    private final AppointmentDAO appointmentDAO;

    public MedicalRecordServiceImpl(MedicalRecordDAO medicalRecordDAO, AppointmentDAO appointmentDAO) {
        this.medicalRecordDAO = medicalRecordDAO;
        this.appointmentDAO = appointmentDAO;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordDAO.findAll();
    }

    @Override
    public MedicalRecord getMedicalRecordById(Long id) {
        MedicalRecord record = medicalRecordDAO.findById(id);
        if (record == null) {
            throw new RuntimeException("Không tìm thấy hồ sơ bệnh án với ID: " + id);
        }
        return record;
    }

    @Override
    public MedicalRecord createFromAppointment(Long appointmentId, String diagnosis, String notes) {
        if (appointmentId == null) {
            throw new RuntimeException("Mã lịch hẹn không được để trống!");
        }

        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("Lịch hẹn không tồn tại!");
        }
        if (appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Không thể tạo hồ sơ cho lịch hẹn đã hủy!");
        }
        if (medicalRecordDAO.findByAppointmentId(appointmentId) != null) {
            throw new RuntimeException("Lịch hẹn này đã có hồ sơ bệnh án!");
        }

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appointment);
        record.setDiagnosis(diagnosis);
        record.setNotes(notes);
        medicalRecordDAO.save(record);
        return record;
    }

    @Override
    public void updateMedicalRecord(MedicalRecord medicalRecord) {
        if (medicalRecord == null || medicalRecord.getId() == null) {
            throw new RuntimeException("Hồ sơ không hợp lệ!");
        }
        if (medicalRecordDAO.findById(medicalRecord.getId()) == null) {
            throw new RuntimeException("Hồ sơ bệnh án không tồn tại!");
        }
        medicalRecordDAO.save(medicalRecord);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        if (medicalRecordDAO.findById(id) == null) {
            throw new RuntimeException("Không tìm thấy hồ sơ để xóa!");
        }
        medicalRecordDAO.delete(id);
    }
}
