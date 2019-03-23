package cn.edu.sicau.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

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
