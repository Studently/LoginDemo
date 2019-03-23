package cn.edu.sicau.web.servlet;

import cn.edu.sicau.utils.VerifyCode;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class VerifyCodeServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//创建验证码类
		VerifyCode vc=new VerifyCode();
		//得到验证码图片
		BufferedImage image=vc.getImage();
		//将图片上的文本保存到session中
		request.getSession().setAttribute("verify_code", vc.getText());
		//把图片响应给客户端
		VerifyCode.output(image, response.getOutputStream());
	}
}
