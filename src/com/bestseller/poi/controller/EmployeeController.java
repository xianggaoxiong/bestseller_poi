package com.bestseller.poi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bestseller.poi.entity.Employee;
import com.bestseller.poi.page.Page;
import com.bestseller.poi.service.EmployeeService;

@Controller
@RequestMapping(value="/employee")
public class EmployeeController extends BaseController{
	 private EmployeeService employeeService;  
     
	  
	    public EmployeeService getEmployeeService() {  
	        return employeeService;  
	    }  
	      
	    @Resource  
	    public void setUserService(EmployeeService employeeService) {  
	        this.employeeService = employeeService;  
	    }     
	  
	      
	    @RequestMapping("/userList")  
	    public String userList(HttpServletRequest request,Map<String,Object> map){  
	        Map<String,Object> params = new HashMap<String,Object>();  
	        //添加查询条件  
	        // ... params.put("name","jack");...
	        //params.put("employeeName", "郑立波");
	        //params.put("gender", "0");
	          
	        //获取总条数  
	        Long totalCount = this.getEmployeeService().pageCounts(params);  
	        //设置分页对象  
	        Page page = executePage(request,totalCount);          
	        //如排序  
	        if(page.isSort()){  
	            params.put("orderName",page.getSortName());   
	            params.put("descAsc",page.getSortState());  
	        }else{  
	            //没有进行排序,默认排序方式  
	            params.put("orderName","employee_id");    
	            params.put("descAsc","asc");  
	        }  
	        //压入查询参数:开始条数与结束条灵敏  
	        params.put("startIndex", page.getBeginIndex());  
	        params.put("everyPage", page.getEveryPage());  
	          
	               
	        //查询集合        
	        List<Employee> users = this.getEmployeeService().pageList(params);
	        System.out.println(users.size());
	        map.put("userList",users);               
	        return "userList";  
	    }  
	} 