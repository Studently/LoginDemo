package cn.edu.sicau.dao;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import cn.edu.sicau.domain.User;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

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
	

}
