package cn.edu.sicau.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.sicau.domain.User;
import cn.edu.sicau.exception.UserException;
import cn.edu.sicau.service.UserService;
import cn.edu.sicau.utils.CommonUtils;

public class RegistServlet extends HttpServlet {

	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");//设置Post接受数据的编码为utf-8
		response.setContentType("text/html;charset=utf-8");//设置Post响应数据的编码为utf-8
		
		
		//依赖UserService
		UserService userService=new UserService();
		
		/**
		 * 封装表单数据(封装到user对象中)
		 * CommonUtils是自己封装的一个jar包中的类，toBean方法将客户端请求数据map，自动封装到user对象中
		 * 但是需要注意：前端form中的name必须和封装的javaBean变量名相同
		 */
		User form= CommonUtils.toBean(request.getParameterMap(), User.class);
		
		
		/**
		 * 表单校验
		 * 使用一个map对象，封装错误信息
		 * 如果表单中的信息不满足条件，将错误信息保存到map中
		 */
		Map<String,String>errors=new HashMap<String,String>();
		
		//验证用户名
		String userName=form.getUsername();
		if(userName==null||userName.trim().isEmpty()){
			errors.put("username", "用户名不能为空！");
		}else if(userName.length()<2||userName.length()>15){
			errors.put("username", "用户名长度必须在2-15之间");
		}
		
		//验证密码
		String passWord=form.getPassword();
		if(passWord==null||passWord.trim().isEmpty()){
			errors.put("password", "密码不能为空！");
		}else if(passWord.length()<4||passWord.length()>15){
			errors.put("password", "密码长度必须在4-15之间");
		}
		
		//验证验证码
		String sessionVerifyCode=(String) request.getSession().getAttribute("verify_code");
		String verifyCode=form.getVerifyCode();
		if(verifyCode==null||verifyCode.trim().isEmpty()){
			errors.put("verifyCode", "验证码不能为空！");
		}else if(!sessionVerifyCode.equalsIgnoreCase(form.getVerifyCode())){
			errors.put("verifyCode", "验证码错误！");
		}
		
		
		//判断errors不为空
		if(errors!=null && errors.size()>0){
			//将errors保存到request中
			request.setAttribute("errors", errors);
			request.setAttribute("user", form);//regist.jsp返回显示
			//转发到regist.jsp
			request.getRequestDispatcher("/user/regist.jsp").forward(request, response);
			return;
		}
		
		
		
		/**
		 * 调用UserService方法的regist方法，将form传入
		 * 1、如果抛出异常，将异常信息保存到request域中
		 * 2、如果没有得到异常，注册成功
		 */
		
		
		try {
			userService.regist(form);
			//注册成功，转到登陆界面
			response.getWriter().print("<h1>注册成功！<h1/><a href='"
			+request.getContextPath()+"/user/login.jsp"+"'>点击这里去登陆<a/>");
			
		} catch (UserException e) {
			//保存异常信息到request域中
			request.setAttribute("msg", e.getMessage());
			
			//将form信息保存到request域中，用来回显
			request.setAttribute("user", form);
			//转发到regist.jsp
			request.getRequestDispatcher("/user/regist.jsp").forward(request, response);
		}
		
	}

}
