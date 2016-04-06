package com.bestseller.poi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bestseller.poi.service.EmployeeService;

@Controller
public class FileDownloadAndUpload {
	@Autowired
	private EmployeeService employeeService;
	
	//数据的导出
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public ResponseEntity<byte[]> testResponseEntity(HttpServletRequest request) throws IOException {

		ServletContext context = request.getSession().getServletContext();
		String filePath = context.getRealPath("/files/" + System.currentTimeMillis() + ".xlsx");
		System.out.println(filePath);
		request.setAttribute("filePath", filePath);
		employeeService.downLoad(filePath);
		
		
		FileInputStream input = new FileInputStream(new File(filePath));
		byte[] body = new byte[input.available()];
		input.read(body);
		input.close();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/vnd.ms-excel");
		headers.add("Content-Disposition", "attachment;filename=employees.xlsx");
		HttpStatus statusCode = HttpStatus.OK;

		ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
		return entity;
	}
	
	//数据的导入
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public String upload(@RequestParam(value="file") MultipartFile file) throws Exception{
		
		CommonsMultipartFile cf= (CommonsMultipartFile)file; 
	    DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
	    File f = fi.getStoreLocation();
	    List<String[]> upload = employeeService.upload(f);
	    
	    if(upload!=null){
	    	for (String[] strings : upload) {
				for(int i=0;i<strings.length;i++){
					System.out.print(strings[i]+"\t");
				}
				System.out.println();
			}
	    }
		return "success";
	}
}
