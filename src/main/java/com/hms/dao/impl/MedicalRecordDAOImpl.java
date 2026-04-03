package com.hms.dao.impl;

import com.hms.config.JpaUtil;
import com.hms.dao.MedicalRecordDAO;
import com.hms.model.entity.MedicalRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class MedicalRecordDAOImpl implements MedicalRecordDAO {

    @Override
    public List<MedicalRecord> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT m FROM MedicalRecord m "
                            + "JOIN FETCH m.appointment a "
                            + "JOIN FETCH a.patient "
                            + "JOIN FETCH a.doctor",
                    MedicalRecord.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public MedicalRecord findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(MedicalRecord.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public MedicalRecord findByAppointmentId(Long appointmentId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<MedicalRecord> list = em.createQuery(
                    "SELECT m FROM MedicalRecord m WHERE m.appointment.id = :aid",
                    MedicalRecord.class
            ).setParameter("aid", appointmentId).setMaxResults(1).getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(MedicalRecord medicalRecord) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            if (medicalRecord.getId() == null) {
                em.persist(medicalRecord);
            } else {
                em.merge(medicalRecord);
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
            MedicalRecord medicalRecord = em.find(MedicalRecord.class, id);
            if (medicalRecord != null) {
                em.remove(em.contains(medicalRecord) ? medicalRecord : em.merge(medicalRecord));
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
