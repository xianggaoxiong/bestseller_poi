package com.bestseller.poi.dao;

import java.util.List;

import com.bestseller.poi.entity.Role;

public interface RoleMapper {
	List<Role> getBy(String[] arrays);
}
