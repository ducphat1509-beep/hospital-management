package com.hms.dao.impl;

import com.hms.config.JpaUtil;
import com.hms.dao.MedicineDAO;
import com.hms.model.entity.Medicine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class MedicineDAOImpl implements MedicineDAO {

    @Override
    public List<Medicine> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT m FROM Medicine m", Medicine.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Medicine findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Medicine.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Medicine medicine) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            if (medicine.getId() == null) {
                em.persist(medicine);
            } else {
                em.merge(medicine);
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
            Medicine medicine = em.find(Medicine.class, id);
            if (medicine != null) {
                em.remove(em.contains(medicine) ? medicine : em.merge(medicine));
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
