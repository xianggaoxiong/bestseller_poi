<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <%@ include file="/common/meta.jsp"%>
  </head>  
  <body>
      <table width="60%" border="1" cellpadding="0" align="center">
			<thead>
				<tr>
					<th style="cursor: hand;" title="按姓名进行排序" onclick="sortPage('employee_name')" valign="top">
						姓名<font color='red'>${page.sortName eq "name" ? page.sortInfo : page.defaultInfo}</font>
					</th>
					<th style="cursor: hand;" title="按出生进行排序" onclick="sortPage('birth')" valign="top">
						年龄<font color='red'>${page.sortName eq "age" ? page.sortInfo : page.defaultInfo}</font>
					</th>
					
					<th style="cursor: hand;" title="按地址进行排序" onclick="sortPage('email')" valign="top">
						地址<font color='red'>${page.sortName eq "address" ? page.sortInfo : page.defaultInfo}</font>
					</th>
					<!-- <th style="cursor: hand;" >
						操作
					</th> -->
				</tr>
			</thead>
			<tbody>			

				<c:forEach items="${userList}" var="user">
					<tr align="center">
						<td>
							${user.employeeName}
						</td>
						<td>
							${user.birth}
						</td>
						
						<td>
							${user.email}
						</td>
						<%-- <td>
							<a
								href="${pageContext.request.contextPath}/user/toAddUser.do">添加</a>
							|
							<a
								href="${pageContext.request.contextPath}/user/getUser/${user.id}.do">编辑</a>
							|
							<a
								href="${pageContext.request.contextPath}/user/delUser/${user.id}.do">删除</a>
						</td> --%>
					</tr>
				</c:forEach>
				<tr>
				<jsp:include page="/page/page.jsp">
					<jsp:param name="url" value="employee/userList.do" />					
				</jsp:include>
				</tr>
			</tbody>
		</table>
		<br>
		<a href="${pageContext.request.contextPath}/index.jsp">返回</a>		
  </body>
</html>
