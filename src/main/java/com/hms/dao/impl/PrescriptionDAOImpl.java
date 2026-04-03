package com.hms.dao.impl;

import com.hms.config.JpaUtil;
import com.hms.dao.PrescriptionDAO;
import com.hms.model.entity.Prescription;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class PrescriptionDAOImpl implements PrescriptionDAO {

    @Override
    public List<Prescription> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT p FROM Prescription p "
                            + "JOIN FETCH p.medicalRecord m "
                            + "JOIN FETCH m.appointment a "
                            + "JOIN FETCH a.patient "
                            + "JOIN FETCH a.doctor",
                    Prescription.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Prescription findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Prescription.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public Prescription findByMedicalRecordId(Long medicalRecordId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Prescription> list = em.createQuery(
                    "SELECT p FROM Prescription p WHERE p.medicalRecord.id = :rid",
                    Prescription.class
            ).setParameter("rid", medicalRecordId).setMaxResults(1).getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Prescription prescription) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            if (prescription.getId() == null) {
                em.persist(prescription);
            } else {
                em.merge(prescription);
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
            Prescription prescription = em.find(Prescription.class, id);
            if (prescription != null) {
                em.remove(em.contains(prescription) ? prescription : em.merge(prescription));
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
