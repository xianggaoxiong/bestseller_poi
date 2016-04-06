package com.bestseller.poi.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bestseller.poi.dao.DepartmentMapper;
import com.bestseller.poi.dao.RoleMapper;
import com.bestseller.poi.entity.Role;
import com.bestseller.poi.service.RoleService;

public class TestPoi {
	/*private RoleMapper roleMapper=null;
	private ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
	
	@Test
	public void test01(){
		roleMapper=ac.getBean(RoleMapper.class);
		String[] arrays=new String[]{"管理员","讲师","班主任"};
		System.out.println(roleMapper.getBy(arrays).size());
	}*/
	
	public static void main(String[] args) {
		DepartmentMapper mapper=null;
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		mapper=ac.getBean(DepartmentMapper.class);
		System.out.println(mapper.getByName("开发部"));
	
	}
	
}
