# LoginDemo
版本1.0
实现简单的注册登陆，用户数据保存在user.xml文件中。其中注册有验证码验证
在web目录下的user目录下有三个jsp页面

关系是：注册-》登陆-》欢迎

java代码部分：
dao:数据库操作层（这里的数据库使用的是xml文件）
domain:数据对象（这里只有user）
exception:自定义的异常类
service:业务操作类
testUser:测试使用操作的测试类
utils:包含一些工具类
web.servlet:登陆、注册、验证码生成三个servlet
下面来逐一介绍：

首先是数据对象domain目录下的user.java

	public class User {
		//用户名
		private String username;
		//密码
		private String password;
		//验证码图片对应的文本
		private String verifyCode;

		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getVerifyCode() {
			return verifyCode;
		}
		public void setVerifyCode(String verifyCode) {
			this.verifyCode = verifyCode;
		}
	}

用户注册时需要匹配填写的验证码和后台生成的是否一致



首先是对数据库文件的操作Dao层下的userDao.java

	public class UserDao {
		//数据库文件的路径
		private String path="G:/users.xml";
		/**
		 * 通过用户名查询user对象
		 * @param username
		 * @return
		 */
		public User findByUsername(String username){

			/**
			 * Xpath查询
			 * 1通过SAXReader 得到document
			 * 2通过document查询用户
			 * 		如果不存在，返回null
			 * 		如果存在，将数据取出封装到user对象中，并返回
			 */

			//创建解析器
			SAXReader reader=new SAXReader();

			try {
				//得到document对象
				Document doc=reader.read(path);

				/**
				 *  selectSingleNode单个查询
				 *  //:无限深度查询
				 *  []:查询条件
				 *  //user[@username='"+user.getUsername()+"']"代表：无限深度查询，查询的元素为user,条件是username等于什么条件
				 */
				Element ele=(Element) doc.selectSingleNode("//user[@username='"+username+"']");
				if(ele==null) return null;

				//如果ele不为空，取出其中数据
				User u=new User();
				String attrUsername=ele.attributeValue("username");
				String attrPassword=ele.attributeValue("password");
				//封装到user对象中
				u.setUsername(attrUsername);
				u.setPassword(attrPassword);
				return u;
			} catch (DocumentException e) {
				throw new RuntimeException(e);
			}

		}


		/**
		 * 添加用户
		 * @param user
		 */
		public void addUser(User user){
			/*
			 * 1. 得到Document
			 * 2. 通过Document得到root元素！<users>
			 * 3. 使用参数user，转发成Element对象
			 * 4. 把element添加到root元素中
			 * 5. 保存Document
			 */

			//创建解析器
			SAXReader reader=new SAXReader();

			try {
				Document doc=reader.read(path);
				//得到根元素
				Element root=doc.getRootElement();

				//ͨ添加user元素
				Element ele=root.addElement("user");

				//向新添加的元素中添加数据
				ele.addAttribute("username", user.getUsername());
				ele.addAttribute("password", user.getPassword());

				//创建输出格式化器
				OutputFormat format=new OutputFormat("\t",true);//缩进使用\t，是否换行，使用是！
				format.setTrimText(true);//清空原有的换行和缩进

				//创建XmlWriter
				XMLWriter writer;

				try {
					writer=new XMLWriter(new OutputStreamWriter(
							new FileOutputStream(path),"utf-8"),format);
					writer.write(doc);
					writer.close();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			} catch (DocumentException e) {
				throw new RuntimeException(e);
			}


	}
自定义异常类：exception目录下的userException.java

	public class UserException extends Exception {

		/**
		 *	自定义一个异常类
		 *		只需要继承构造方法即可，方便创建对象
		 */
		public UserException() {
			super();
			// TODO Auto-generated constructor stub
		}

		public UserException(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public UserException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}

		public UserException(Throwable cause) {
			super(cause);
			// TODO Auto-generated constructor stub
		}
	}
用户异常包括登陆时，用户不存在；注册时，用户已存在等，返回的异常信息用来显示在客户端浏览器中

接着是业务类：service目录下的userService.java

	public class UserService {
		//依赖UserDao
		private UserDao userDao=new UserDao();
		/**
		 *注册功能
		 * @param user
		 * @throws UserException
		 */
		public void regist(User user) throws UserException {

			/**
			 * 查询该用户信息，如果存在抛出异常
			 * 
			 */

			User _user=userDao.findByUsername(user.getUsername());
			if(_user!=null){
				throw new UserException(user.getUsername()+"：已经被注册过了");
			}

			//否则，添加用户
			userDao.addUser(user);
		}

		/**
		 * 登陆
		 * @param user
		 * @throws UserException
		 */
		public User login(User user) throws UserException{
			/**
			 * 查询该用户信息，如果存在抛出异常
			 * 
			 */

			User _user=userDao.findByUsername(user.getUsername());

			//如果用户不存在
			if(_user==null){
				throw new UserException("该用户不存在！");
			}

			//如果用户的密码错误
			if(!_user.getPassword().equals(user.getPassword())){
				throw new UserException("密码错误！");
			}

			//如果用户名存在，且密码正确，返回查询对象（因为用户信息可能不仅仅只有名称和密码）
			return _user;
		}

	}
接着是注册的前端页面：regist.jsp，不过在进入regist.jsp界面前需要完成 验证码生成工具类：VerifyCode.java和servlet:VerifyCodeServlet.java

VerifyCode.java(工具类积累)

	public class VerifyCode {
	    private int w = 70;
	    private int h = 35;
	    private Random r = new Random();
	    // {"宋体", "华文楷体", "黑体", "华文新魏", "华文隶书", "微软雅黑", "楷体_GB2312"}
	    private String[] fontNames  = {"宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};
	    // 可选字符
	    private String codes  = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
	    // 背景色
	    private Color bgColor  = new Color(255, 255, 255);
	    // 验证码上的文本
	    private String text ;

	    // 生成随机的颜色
	    private Color randomColor () {
		int red = r.nextInt(150);
		int green = r.nextInt(150);
		int blue = r.nextInt(150);
		return new Color(red, green, blue);
	    }

	    // 生成随机的字体
	    private Font randomFont () {
		int index = r.nextInt(fontNames.length);
		String fontName = fontNames[index];//生成随机的字体名称
		int style = r.nextInt(4);//生成随机的样式, 0(无样式), 1(粗体), 2(斜体), 3(粗体+斜体)
		int size = r.nextInt(5) + 24; //生成随机字号, 24 ~ 28
		return new Font(fontName, style, size);
	    }

	    // 画干扰线
	    private void drawLine (BufferedImage image) {
		int num  = 3;//一共画3条
		Graphics2D g2 = (Graphics2D)image.getGraphics();
		for(int i = 0; i < num; i++) {//生成两个点的坐标，即4个值
		    int x1 = r.nextInt(w);
		    int y1 = r.nextInt(h);
		    int x2 = r.nextInt(w);
		    int y2 = r.nextInt(h);
		    g2.setStroke(new BasicStroke(1.5F));
		    g2.setColor(Color.BLUE); //干扰线是蓝色
		    g2.drawLine(x1, y1, x2, y2);//画线
		}
	    }

	    // 随机生成一个字符
	    private char randomChar () {
		int index = r.nextInt(codes.length());
		return codes.charAt(index);
	    }

	    // 创建BufferedImage
	    private BufferedImage createImage () {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D)image.getGraphics();
		g2.setColor(this.bgColor);
		g2.fillRect(0, 0, w, h);
		return image;
	    }

	    // 调用这个方法得到验证码
	    public BufferedImage getImage () {
		BufferedImage image = createImage();//创建图片缓冲区
		Graphics2D g2 = (Graphics2D)image.getGraphics();//得到绘制环境
		StringBuilder sb = new StringBuilder();//用来装载生成的验证码文本
		// 向图片中画4个字符
		for(int i = 0; i < 4; i++)  {//循环四次，每次生成一个字符
		    String s = randomChar() + "";//随机生成一个字母
		    sb.append(s); //把字母添加到sb中
		    float x = i * 1.0F * w / 4; //设置当前字符的x轴坐标
		    g2.setFont(randomFont()); //设置随机字体
		    g2.setColor(randomColor()); //设置随机颜色
		    g2.drawString(s, x, h-5); //画图
		}
		this.text = sb.toString(); //把生成的字符串赋给了this.text
		drawLine(image); //添加干扰线
		return image;
	    }

	    // 返回验证码图片上的文本
	    public String getText () {
		return text;
	    }

	    // 保存图片到指定的输出流
	    public static void output (BufferedImage image, OutputStream out)
		    throws IOException {
		//ImageIO.write(image, "JPEG", out);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(image);
	    }
	}
VerifyCodeServlet.java

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
 regist.jsp

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
注册点击按钮后会请求服务器的对应servlet:RegistServlet.java，在RegistServlet会用到一个自己写的工具类CommonUtils

CommonUtils.java

	public class CommonUtils {

	    /**
	     * 传入的map转换成对应的JavaBean对象
	     * @param map
	     * @param clazz
	     * @param <T>
	     * @return
	     */
	    public static <T> T toBean(Map map,Class<T> clazz){
		try {
		    //获得对应的JavaBean对象
		    T bean=clazz.newInstance();
		    //把数据封装到JavaBean中
		    BeanUtils.populate(bean,map);
		    //返回Javabean
		    return bean;
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
	    }
	}
RegistServlet.java

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

登陆注册成功后，进入登陆界面login.jsp

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
登陆界面请求服务器的LoginServlet.java

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

至此，项目走完了
