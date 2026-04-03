package com.hms.dao.impl;

import com.hms.config.JpaUtil;
import com.hms.dao.BillMedicineDetailDAO;
import com.hms.model.entity.BillMedicineDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class BillMedicineDetailDAOImpl implements BillMedicineDetailDAO {

    @Override
    public List<BillMedicineDetail> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT bmd FROM BillMedicineDetail bmd JOIN FETCH bmd.bill JOIN FETCH bmd.medicine",
                    BillMedicineDetail.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public BillMedicineDetail findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<BillMedicineDetail> list = em.createQuery(
                    "SELECT d FROM BillMedicineDetail d JOIN FETCH d.medicine JOIN FETCH d.bill WHERE d.id = :id",
                    BillMedicineDetail.class
            ).setParameter("id", id).getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<BillMedicineDetail> findByBillId(Long billId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT d FROM BillMedicineDetail d WHERE d.bill.id = :bid",
                    BillMedicineDetail.class
            ).setParameter("bid", billId).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public BillMedicineDetail findByBillAndMedicine(Long billId, Long medicineId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<BillMedicineDetail> list = em.createQuery(
                    "SELECT d FROM BillMedicineDetail d WHERE d.bill.id = :bid AND d.medicine.id = :mid",
                    BillMedicineDetail.class
            ).setParameter("bid", billId).setParameter("mid", medicineId).setMaxResults(1).getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(BillMedicineDetail detail) {
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
            BillMedicineDetail detail = em.find(BillMedicineDetail.class, id);
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
