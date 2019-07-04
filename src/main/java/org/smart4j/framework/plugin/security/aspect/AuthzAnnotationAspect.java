package org.smart4j.framework.plugin.security.aspect;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.aop.AspectProxy;
import org.smart4j.framework.aop.annotation.Aspect;
import org.smart4j.framework.plugin.security.annotation.User;
import org.smart4j.framework.plugin.security.exception.AuthzException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 授权注解切面
 */
@Aspect(Controller.class)
public class AuthzAnnotationAspect extends AspectProxy {
    /**
     * 定义一个机遇授权功能的注解数组
     */
    private static final Class[] ANNOTATION_CLASS_ARRAY = {
            User.class
    };

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        //从目标类与目标方法中获取相应的注解
        Annotation annotation = getAnnotation(cls, method);
        if (annotation != null) {
            Class<?> annotationType = annotation.annotationType();
            if (annotationType.equals(User.class)) {
                handleUser();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Annotation getAnnotation(Class<?> cls, Method method) {
        //遍历所有的授权注解
        for (Class<? extends Annotation> annotationClass : ANNOTATION_CLASS_ARRAY) {
            //首先判断目标方法上是否有授权注解
            if (method.isAnnotationPresent(annotationClass)) {
                return method.getAnnotation(annotationClass);
            }
            //然后判断目标类上是否带有授权注解
            if (cls.isAnnotationPresent(annotationClass)) {
                return cls.getAnnotation(annotationClass);
            }
        }
        //若目标方法与目标类上都未带授权注解,则返回空对象
        return null;
    }

    private void handleUser() {
        Subject currentUser = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = currentUser.getPrincipals();
        if (principalCollection == null || principalCollection.isEmpty()) {
            throw new AuthzException("当前用户尚未登录");
        }
    }
}
