package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bean助手类
 */
public class BeanHelper {

    /**
     * 定义 Bean 映射(用于存放Bean类与Bean实例的映射关系)
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        try {
            Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
            for(Class<?> beanClass : beanClassSet) {
                if(beanClass.isAnnotationPresent(Controller.class) || beanClass.isAnnotationPresent(Service.class)) {
                    Object obj = ReflectionUtil.newInstance(beanClass);
                    BEAN_MAP.put(beanClass, obj);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化 BeanHelper 出错！",e);
        }

    }

    /**
     * 获取Bean映射
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取Bean实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls) {
        if(!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get bean by class: " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    /**
     * 设置Bean实例
     */
    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls,obj);
    }
}
