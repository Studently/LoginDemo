package cn.edu.sicau.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    private static Properties pro=null;

    static {
        try {
            //实例pro
            pro=new Properties();
            InputStream is=JDBCUtils.class.getClassLoader()
                    .getResourceAsStream("jdbc.properties");
            pro.load(is);
            Class.forName(pro.getProperty("driverClassName"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取JDBC连接对象
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {

        String url=pro.getProperty("url");
        String username=pro.getProperty("username");
        String password=pro.getProperty("password");

        return DriverManager.getConnection(url,username,password);

    }
}
