package com.hms.dao.impl;

import com.hms.config.JpaUtil;
import com.hms.dao.DoctorDAO;
import com.hms.model.entity.Doctor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class DoctorDAOImpl implements DoctorDAO {

    @Override
    public List<Doctor> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Thêm "JOIN FETCH d.department" để lấy luôn dữ liệu khoa
            return em.createQuery(
                    "SELECT d FROM Doctor d LEFT JOIN FETCH d.department",
                    Doctor.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Doctor findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Doctor.class, id); // JPA có sẵn, đỡ phải viết query
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Doctor doctor) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(doctor);
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

            Doctor doctor = em.find(Doctor.class, id);
            if (doctor != null) {
                em.remove(em.contains(doctor) ? doctor : em.merge(doctor));
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