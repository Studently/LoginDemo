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

具体讲解参考：
https://blog.csdn.net/SICAUliuy/article/details/88760948

版本2.0
讲解参考
https://blog.csdn.net/SICAUliuy/article/details/88764157

