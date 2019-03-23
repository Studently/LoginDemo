<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>登陆</title>
</head>
<body>
<p style="color:red;font-weight: 900">${msg }</p>
<form action="<c:url value='/LoginServlet'/>" method="post">
	用户名: <input type="text" name="username" value="${user.username }"/><br/>
	密    码: <input type="password" name="password" value="${user.password }"/><br/>
	<input type="submit" value="登陆"/>
</form>
</body>
</html>