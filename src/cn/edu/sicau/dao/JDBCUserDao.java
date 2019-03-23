package cn.edu.sicau.dao;

import cn.edu.sicau.domain.User;
import cn.edu.sicau.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUserDao implements UserDao{
    @Override
    public User findByUsername(String username) {
        Connection cn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            //获取数据库连接对象
            cn= JDBCUtils.getConnection();

            //创建sql模板
            String sql="select * from jdbcUser where username=?";
            //获得prepareStatement对象
            ps=cn.prepareStatement(sql);

            ps.setString(1,username);
            rs=ps.executeQuery();

            if(rs==null){
                return null;
            }
            if(rs.next()){
                //转换成user对象并返回
                User user=new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;

            }else{
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
                if(cn!=null) cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void addUser(User user) {

        Connection cn=null;
        PreparedStatement ps=null;
        try {
            //获取数据库连接对象
            cn= JDBCUtils.getConnection();

            //创建sql模板
            String sql="insert into jdbcUser(username,password) values(?,?)";
            //获得prepareStatement对象
            ps=cn.prepareStatement(sql);

            ps.setString(1,user.getUsername());
            ps.setString(2,user.getPassword());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if(ps!=null) ps.close();
                if(cn!=null) cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
