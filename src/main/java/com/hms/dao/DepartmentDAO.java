package com.hms.dao;

import com.hms.model.entity.Department;
import java.util.List;

public interface DepartmentDAO {
    List<Department> findAll();
}