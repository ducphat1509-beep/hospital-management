package com.hms.dao;

import com.hms.model.entity.Appointment;
import java.util.List;

public interface AppointmentDAO {
    List<Appointment> findAll();
    Appointment findById(Long id);
    void save(Appointment appointment);
    void delete(Long id);
}