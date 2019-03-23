package cn.edu.sicau.dao;

import cn.edu.sicau.domain.User;

public interface UserDao {

    //通过用户名查询用户信息
    public User findByUsername(String username);
    //添加用户信息
    public void addUser(User user);
}
