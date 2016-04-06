package com.bestseller.poi.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bestseller.poi.dao.DepartmentMapper;
import com.bestseller.poi.dao.EmployeeMapper;
import com.bestseller.poi.dao.RoleMapper;
import com.bestseller.poi.entity.Department;
import com.bestseller.poi.entity.Employee;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeMapper dao;
	@Autowired
	private DepartmentMapper departmentDao;
	@Autowired
	private RoleMapper roleMapper;
	
	
	
	@Transactional(readOnly=true)
	public void downLoad(String filePath) throws IOException {
		Workbook wb = new XSSFWorkbook(); // or new HSSFWorkbook();
		Sheet sheet = wb.createSheet("employees");

		// 1. 填充标题行.
		createTitleRow(sheet);

		// 2. 填充 Employee 信息
		createEmployeeRows(sheet);

		// 3. 设置样式: 包括行高, 列宽, 边框.
		setCellStyle(wb);

		// 4. 把 Excel 保存到磁盘上.
		FileOutputStream fileOut = new FileOutputStream(filePath);
		wb.write(fileOut);
		fileOut.close();
	}
	
	//设置单元格的间距
	private void setCellStyle(Workbook wb) {
		Sheet sheet = wb.getSheet("employees");

		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			row.setHeightInPoints(25);

			for (int j = 0; j < TITLES.length; j++) {
				Cell cell = row.getCell(j);
				cell.setCellStyle(getCellStyle(wb));
			}
		}

		for (int j = 0; j < TITLES.length; j++) {
			sheet.autoSizeColumn(j);
			sheet.setColumnWidth(j, (int) (sheet.getColumnWidth(j) * 1.3));
		}
	}
	//单元格样式的设置
	private CellStyle getCellStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());

		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());

		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());

		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());

		return style;
	}
	//为每个单元格填充数据
	private void createEmployeeRows(Sheet sheet) {
		List<Employee> emps = dao.getAll();
		for (int i = 0; i < emps.size(); i++) {
			Employee emp = emps.get(i);
			Row row = sheet.createRow(i + 1);

			Cell cell = row.createCell(0);
			cell.setCellValue("" + (i + 1));

			cell = row.createCell(1);
			cell.setCellValue(emp.getLoginName());

			cell = row.createCell(2);
			cell.setCellValue(emp.getEmployeeName());

			cell = row.createCell(3);
			cell.setCellValue(emp.getGender());

			cell = row.createCell(4);
			cell.setCellValue(emp.getEnabled());

			cell = row.createCell(5);
			cell.setCellValue(emp.getDepartment().getDepartmentName());

			cell = row.createCell(6);
			cell.setCellValue(emp.getEmail());

			cell = row.createCell(7);
			cell.setCellValue(emp.getRoleNames());
		}
	}
	
	//设置行的样式
	private static final String[] TITLES=new String[]{"序号", "登录名", "姓名",
			"性别", "登录许可", "部门", "E-mail", "角色"}; 
	private void createTitleRow(Sheet sheet) {
		Row row = sheet.createRow(0);
		for(int i=0;i<TITLES.length;i++){
			row.createCell(i).setCellValue(TITLES[i]);
		}
	}
	
	
	
	
	//文件的上传
	@Transactional
	public List<String[]> upload(File file) throws Exception {
		InputStream is=new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(is);

		// 2. 得到 Sheet 对象
		Sheet sheet = wb.getSheet("employees");

		// 3. 解析数据: 解析每一行, 每一个单元格.
		// 把一行转为一个 Employee 对象, 把整个 Excel 文档解析为一个 Employee 的集合
		// 若出现错误, 则把错误返回
		List<Employee> employees = new ArrayList<>();
		List<String[]> errors = parseToEmployeeList(sheet, employees);

		if (errors != null && errors.size() > 0) {
			return errors;
		}
		// 4. 批量插入
		dao.batchSave(employees);
		
		// 5. 返回错误.
		return null;
	}

	private List<String[]> parseToEmployeeList(Sheet sheet, List<Employee> employees) {
		
		List<String[]> errors = new ArrayList<>();

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			Employee employee = parseToEmployee(row, i + 1, errors);

			if (employee != null) {
				employees.add(employee);
			}
		}

		return errors;
	}
	
	private Employee parseToEmployee(Row row, int i, List<String[]> errors) {
		
		Employee employee = null;

		// 登录名 姓名 性别 登录许可 部门 E-mail 角色

		// 获取 loginName, 并对其进行验证
		Cell cell = row.getCell(1);
		String loginName = getCellValue(cell);
		boolean flag = validateLoginName(loginName);
		if (!flag) {
			errors.add(new String[] { i + "", "2" });
		}

		cell = row.getCell(2);
		String employeeName = getCellValue(cell);

		cell = row.getCell(3);
		String gender = getCellValue(cell);
		gender = validateGender(gender);
		if (gender == null) {
			errors.add(new String[] { i + "", "4" });
			flag = false;
		}

		cell = row.getCell(4);
		String enabledStr = getCellValue(cell);
		int enabled = validateEnabled(enabledStr);
		if (enabled == -1) {
			errors.add(new String[] { i + "", "5" });
			flag = false;
		}

		cell = row.getCell(5);
		String departmentName = getCellValue(cell);
		Department department = validateDepartment(departmentName);
		if (department == null) {
			errors.add(new String[] { i + "", "6" });
			flag = false;
		}

		cell = row.getCell(6);
		String email = getCellValue(cell);
		if (!valiateEmail(email)) {
			errors.add(new String[] { i + "", "7" });
			flag = false;
		}

		/*cell = row.getCell(7);
		String roleNames = getCellValue(cell);
		Set<Role> roles = validateRoles(roleNames);
		if (roles == null || roles.size() == 0
				|| roles.size() != roleNames.split(",").length) {
			errors.add(new String[] { i + "", "8" });
			flag = false;
		}*/

		if (flag) {
			employee = new Employee(loginName, employeeName, null, enabled,
					department, gender, email);
		}

		return employee;
	}

	
	
	/*private Set<Role> validateRoles(String roleNames) {
		Collection<Role> roles = roleMapper.getBy(roleNames.split(","));
		return new HashSet<>(roles);
	}*/

	private boolean valiateEmail(String email) {
		Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private Department validateDepartment(String departmentName) {
		return departmentDao.getByName(departmentName);
	}

	private int validateEnabled(String enabledStr) {
		try {
			double d = Double.parseDouble(enabledStr);
			if(d == 1.0d || d == 0.0d){
				return (int)d;
			}
		} catch (NumberFormatException e) {}
		
		return -1;
	}

	private String validateGender(String gender) {
		try {
			double d = Double.parseDouble(gender);
			if(d == 1.0d || d == 0.0d){
				return (int)d + "";
			}
		} catch (NumberFormatException e) {}
		return null;
	}

	private boolean validateLoginName(String loginName) {
		if(loginName != null 
				&& !loginName.trim().equals("") 
				&& loginName.trim().length() >= 6){
			//正则验证
			Pattern p = Pattern.compile("^[a-zA-Z]\\w+\\w$");
			Matcher m = p.matcher(loginName.trim());
			boolean b = m.matches();
			
			//验证 loginName 是否合法: 是否在数据表中有对应的记录
			if(b){
				Employee employee = dao.getBy(loginName.trim());
				if(employee == null){
					return true;
				}
			}	
			
		}
		return false;
	}

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private String getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getRichStringCellValue().getString();
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				return dateFormat.format(date)+"";
			} else {
				return cell.getNumericCellValue() + "";
			}
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula() + "";
		}
		return null;
	}

	public Long pageCounts(Map<String, Object> params) {
		return dao.pageCounts(params);
	}

	public List<Employee> pageList(Map<String, Object> params) {
		return dao.pageList(params);
	}

}
