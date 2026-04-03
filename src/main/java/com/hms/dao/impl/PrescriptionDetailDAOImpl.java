package com.hms.dao.impl;

import com.hms.config.JpaUtil;
import com.hms.dao.PrescriptionDetailDAO;
import com.hms.model.entity.PrescriptionDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class PrescriptionDetailDAOImpl implements PrescriptionDetailDAO {

    @Override
    public List<PrescriptionDetail> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT pd FROM PrescriptionDetail pd JOIN FETCH pd.prescription JOIN FETCH pd.medicine",
                    PrescriptionDetail.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public PrescriptionDetail findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(PrescriptionDetail.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public PrescriptionDetail findByPrescriptionAndMedicine(Long prescriptionId, Long medicineId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<PrescriptionDetail> list = em.createQuery(
                    "SELECT pd FROM PrescriptionDetail pd WHERE pd.prescription.id = :pid AND pd.medicine.id = :mid",
                    PrescriptionDetail.class
            ).setParameter("pid", prescriptionId).setParameter("mid", medicineId).setMaxResults(1).getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(PrescriptionDetail detail) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            if (detail.getId() == null) {
                em.persist(detail);
            } else {
                em.merge(detail);
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
            PrescriptionDetail detail = em.find(PrescriptionDetail.class, id);
            if (detail != null) {
                em.remove(em.contains(detail) ? detail : em.merge(detail));
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
