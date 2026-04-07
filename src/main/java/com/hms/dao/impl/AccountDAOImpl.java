package com.hms.dao.impl;

import com.hms.config.JpaUtil;
import com.hms.dao.AccountDAO;
import com.hms.model.entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class AccountDAOImpl implements AccountDAO {
    @Override
    public Account findByUsername(String username) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Account a WHERE a.username = :user", Account.class)
                    .setParameter("user", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Account account) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(account);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}