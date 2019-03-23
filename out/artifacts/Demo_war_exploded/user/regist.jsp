<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script type="text/javascript">
	function _change(){
		
		//通过id获取<imag>元素
		var ele=document.getElementById("vCode");
		/**
		重新请求验证码servlet，因为缓存的原因，需要区别于之前的请求，
		所以在请求路径后添加一个参数xxx,并将参数值设置成当前时间，可以保证唯一
		*/
		ele.src="<c:url value='/VerifyCodeServlet'/>?xxx="+new Date().getTime();
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>注册</title>

</head>
<body>
<h1>注册</h1>
<!--用于显示错误信息  -->
<p style="color:red;font-weight: 900">${msg}</p>
<!--"<c:url value='/RegistServlet'/>" 会在路径前面添加应用名等价于 request.getContextPath +"/RegistServlet"-->
<form action="<c:url value='/RegistServlet'/>" method="post">
	用户名: <input type="text" name="username" value="${user.username }"/>
	${errors.username }<br/>
	密    码: <input type="password" name="password" value="${user.password }"/>
	${errors.password }<br/>
	验证码: <input type="text" name="verifyCode" value="${user.verifyCode }" size="3">
		  <img id="vCode" src="<c:url value='/VerifyCodeServlet'/>" border="1"/>
		  <a href="javascript:_change()">换一张</a>
		  ${errors.verifyCode }<br/>
		  
	
	<input type="submit" value="注册"/>
</form>
</body>
</html>