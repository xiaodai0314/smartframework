package org.smart4j.framework;

import java.lang.reflect.Method;

public class Test {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception{
        Class<?> cls = Class.forName("org.smart4j.framework.Test");
        Object object = cls.newInstance();
        Method method = cls.getMethod("say");
        System.out.println(method.getParameterCount());
        method.invoke(object,null);
    }

    public void say() {
        System.out.println("abac");
    }

}
