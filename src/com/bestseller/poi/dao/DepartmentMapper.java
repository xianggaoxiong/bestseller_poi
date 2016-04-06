package com.bestseller.poi.dao;


import org.apache.ibatis.annotations.Param;

import com.bestseller.poi.entity.Department;

public interface DepartmentMapper {
	Department getById(Integer id);

	Department getByName(@Param("departmentName") String departmentName);
	
}
