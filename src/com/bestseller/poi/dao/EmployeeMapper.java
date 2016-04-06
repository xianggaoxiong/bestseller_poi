package com.bestseller.poi.dao;


import java.util.List;
import java.util.Map;

import com.bestseller.poi.entity.Employee;

public interface EmployeeMapper {
	List<Employee> getAll();

	Employee getBy(String trim);
	
	void batchSave(List<Employee> employees);
	
	public List<Employee> pageList(Map<String,Object> params);  
    //分页总条数  
    public Long pageCounts(Map<String,Object> p); 
}
