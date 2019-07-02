package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Impl;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 依赖注入助手类
 */
public class IocHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(IocHelper.class);
    static {
//        获取所有的Bean类与Bean实例之间的映射关系
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if(CollectionUtil.isNotEmpty(beanMap)) {
//            遍历Bean Map
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                //从BeanMap中获取Bean类与Bean实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取bean类中所有字段,(不包括父类方法)
                Field[] beanFields = beanClass.getDeclaredFields();
                if(ArrayUtil.isNotEmpty(beanFields)) {
                    //遍历Bean Field
                    for(Field beanField : beanFields) {
                        //判断当前Bean Field是否带有Inject注解
                        if(beanField.isAnnotationPresent(Inject.class)) {
                            //在bean Map 中获取 Bean Field对应的实例 获取bean字段对应的接口
                            Class<?> interfaceClass = beanField.getType();
                            //获取 bean字段对应的实现类
                            Class<?> implementClass = findImplementClass(interfaceClass);
                            if(implementClass != null) {
                                //从BEANMAP 获取相应的实例
                                Object implementInstance = beanMap.get(implementClass);
                                if(implementInstance != null) {
                                    /*beanField.setAccessible(true);
                                    beanField.set(beanInstance, implementInstance);*/
                                    //通过反射初始化BeanField的值
                                    LOGGER.debug("beanInstance: " + beanInstance + "  beanField: " + beanField + "implementInstance" +
                                            implementInstance);
                                    //问题3,依赖注入失败. 设置成员的私有变量
                                    ReflectionUtil.setField(beanInstance, beanField, implementInstance);

                                } else {
                                    throw new RuntimeException("依赖注入失败！类名：" + beanClass.getSimpleName() + "，字段名：" + interfaceClass.getSimpleName());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 查找实现类
     * @param interfaceClass
     * @return
     */
    public static Class<?> findImplementClass(Class<?> interfaceClass) {
        Class<?> implementClass = interfaceClass;
        //判断接口是否标注了 impl注解
        if (implementClass.isAnnotationPresent (Impl.class)) {
            //获取强制指定的实现类
            implementClass = interfaceClass.getAnnotation(Impl.class).value();
        } else {
            Set<Class<?>> implementClassList = ClassHelper.getClassSetBySuper(interfaceClass);
            if(CollectionUtil.isNotEmpty(implementClassList)) {
                implementClass = implementClassList.iterator().next();
            }
        }
        return implementClass;
    }

    public static void iocAgain() {
        //        获取所有的Bean类与Bean实例之间的映射关系
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if(CollectionUtil.isNotEmpty(beanMap)) {
//            遍历Bean Map
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                //从BeanMap中获取Bean类与Bean实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取bean类中所有字段,(不包括父类方法)
                Field[] beanFields = beanClass.getDeclaredFields();
                if(ArrayUtil.isNotEmpty(beanFields)) {
                    //遍历Bean Field
                    for(Field beanField : beanFields) {
                        //判断当前Bean Field是否带有Inject注解
                        if(beanField.isAnnotationPresent(Inject.class)) {
                            //在bean Map 中获取 Bean Field对应的实例 获取bean字段对应的接口
                            Class<?> interfaceClass = beanField.getType();
                            //获取 bean字段对应的实现类
                            Class<?> implementClass = findImplementClass(interfaceClass);
                            if(implementClass != null) {
                                //从BEANMAP 获取相应的实例
                                Object implementInstance = beanMap.get(implementClass);
                                if(implementInstance != null) {
                                    /*beanField.setAccessible(true);
                                    beanField.set(beanInstance, implementInstance);*/
                                    //通过反射初始化BeanField的值
                                    LOGGER.debug("beanInstance: " + beanInstance + "  beanField: " + beanField + "implementInstance" +
                                            implementInstance);
                                    //问题3,依赖注入失败. 设置成员的私有变量
                                    ReflectionUtil.setField(beanInstance, beanField, implementInstance);

                                } else {
                                    throw new RuntimeException("依赖注入失败！类名：" + beanClass.getSimpleName() + "，字段名：" + interfaceClass.getSimpleName());
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}
