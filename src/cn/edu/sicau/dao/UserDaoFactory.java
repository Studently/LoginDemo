package cn.edu.sicau.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UserDaoFactory {

    //获取src下dao.properties配置文件
   private static Properties pro=null;
   private static UserDao userDao;

   static{
       try {
           pro=new Properties();
           //将dao.properties加载成输入流
           InputStream is=UserDaoFactory.class.getClassLoader()
                   .getResourceAsStream("dao.properties");
           pro.load(is);//配置文件加载到配置文件类中
           //获得配置文件中的类名通过反射获得类实例
           String calssName=pro.getProperty("cn.edu.sicau.dao.UserDao");
           Class clazz=Class.forName(calssName);
           userDao= (UserDao) clazz.newInstance();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }


    /**
     * 获取userDao对应的实例
     */

   public static UserDao getUserDao(){
       return userDao;
   }

}
