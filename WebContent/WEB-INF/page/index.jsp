<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#downLoad").click(function(){
			$(this).parent().submit();
		});
	});
</script>
</head>
<body>
<form action="${pageContext.request.contextPath }/download" method="get">
	<a id="downLoad" href="#">将数据导出到Excel</a>
</form>


<fieldset>
	<form action="${pageContext.request.contextPath }/upload" method="post" enctype="multipart/form-data">
		将Excel数据导入数据库:<input type="file" name="file"/>
		<input type="submit" value="上传"/>
	</form>
</fieldset>
</body>
</html>