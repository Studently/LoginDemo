package cn.edu.sicau.service;


import cn.edu.sicau.dao.UserDao;
import cn.edu.sicau.domain.User;
import cn.edu.sicau.exception.UserException;

public class UserService {
	
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
