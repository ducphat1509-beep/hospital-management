package com.hms.dao;

import com.hms.model.entity.Department;
import java.util.List;

public interface DepartmentDAO {
    List<Department> findAll();
    Department findById(Long id);
    void save(Department department);
    void delete(Long id);
}
