<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>登陆成功，欢迎你</title>
</head>
<body>
<h1>欢迎登陆本系统</h1>
<c:choose>
	<c:when test="${empty sessionScope.sessionUser }">请从正门走！</c:when>
	<c:otherwise>欢迎：${sessionScope.sessionUser.username }</c:otherwise>
</c:choose>

</body>
</html>