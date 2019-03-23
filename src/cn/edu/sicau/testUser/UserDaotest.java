package cn.edu.sicau.testUser;


import cn.edu.sicau.dao.UserDao;
import cn.edu.sicau.dao.UserDaoFactory;
import cn.edu.sicau.domain.User;
import org.junit.Test;

/**
 * 用来测试
 * @author ly
 *
 */
public class UserDaotest {
	
	
	
	@Test
	public void findByUsername(){
		User user =new User();
		UserDao userDao= UserDaoFactory.getUserDao();
		user.setUsername("李四");
		user.setPassword("lisi");
		
		User u=userDao.findByUsername(user.getUsername());
		System.out.println(u.getUsername());
		
	}
	
	@Test
	public void addUsername(){
		/*User user =new User();
		UserDao userDao=UserDaoFactory.getUserDao();
		user.setUsername("����");
		user.setPassword("lisi");
		
		userDao.addUser(user);*/
		
		
	}

}
