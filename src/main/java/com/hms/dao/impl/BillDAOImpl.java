package com.hms.dao.impl;

import com.hms.config.JpaUtil;
import com.hms.dao.BillDAO;
import com.hms.model.entity.Bill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class BillDAOImpl implements BillDAO {

    @Override
    public List<Bill> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT b FROM Bill b JOIN FETCH b.patient",
                    Bill.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Bill findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Bill.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Bill bill) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            if (bill.getId() == null) {
                em.persist(bill);
            } else {
                em.merge(bill);
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
            Bill bill = em.find(Bill.class, id);
            if (bill != null) {
                em.remove(em.contains(bill) ? bill : em.merge(bill));
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
