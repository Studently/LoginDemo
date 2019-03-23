package cn.edu.sicau.web.servlet;

import java.io.IOException;

import cn.edu.sicau.domain.User;
import cn.edu.sicau.exception.UserException;
import cn.edu.sicau.service.UserService;
import cn.edu.sicau.utils.CommonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginServlet extends HttpServlet {

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");//客户端发送编码(POST)
		response.setContentType("text/html;charset=utf-8");//响应编码
		
		//依赖UserService
		UserService userService=new UserService();
		
		//将表格数据封装到user对象中
		User form= CommonUtils.toBean(request.getParameterMap(), User.class);
		
		try {
			User user=userService.login(form);
			
			//如果没有异常，将用户信息保存到session中
			request.getSession().setAttribute("sessionUser", user);
			//重定向到welcome.jsp页面
			response.sendRedirect(request.getContextPath()+"/user/welcome.jsp");
		} catch (UserException e) {
			//将报错信息添加到request中
			request.setAttribute("msg", e.getMessage());
			//将用户信息保存到request中，用于返回显示
			request.setAttribute("user", form);
			
			//转发到login.jsp
			request.getRequestDispatcher("/user/login.jsp").forward(request, response);
		}
		
		
	}

}
