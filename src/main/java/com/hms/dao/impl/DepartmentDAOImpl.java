package com.hms.dao.impl;

import com.hms.config.JpaUtil;
import com.hms.dao.DepartmentDAO;
import com.hms.model.entity.Department;
import jakarta.persistence.EntityManager;
import java.util.List;

public class DepartmentDAOImpl implements DepartmentDAO {
    @Override
    public List<Department> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT d FROM Department d", Department.class).getResultList();
        } finally {
            em.close();
        }
    }
}