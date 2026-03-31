package com.hms.dao.impl;

import com.hms.config.JpaUtil;
import com.hms.dao.AppointmentDAO;
import com.hms.model.entity.Appointment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public List<Appointment> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // JOIN FETCH giúp lấy luôn đối tượng Patient và Doctor liên quan trong 1 câu lệnh SQL
            return em.createQuery(
                    "SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor",
                    Appointment.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Appointment findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Appointment.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Appointment appointment) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            // Nếu id đã tồn tại thì update (merge), nếu chưa thì tạo mới (persist)
            if (appointment.getId() == null) {
                em.persist(appointment);
            } else {
                em.merge(appointment);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Appointment appointment = em.find(Appointment.class, id);
            if (appointment != null) {
                em.remove(appointment);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}