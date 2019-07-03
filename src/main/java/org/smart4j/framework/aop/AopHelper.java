package org.smart4j.framework.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.aop.annotation.Aspect;
import org.smart4j.framework.aop.proxy.Proxy;
import org.smart4j.framework.aop.proxy.ProxyManager;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.tx.TransactionProxy;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 初始化AOP框架
 */
public class AopHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                BeanHelper.setBean(targetClass, proxy );
            }
        } catch (Exception e) {
            LOGGER.error("aop failure", e);
        }
    }

    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);

        for (Class<?> proxyClass : proxyClassSet) {
            if(proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
//        TODO
        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
        Set<Class<?>> targetClassSet = new HashSet<>();
        Class<? extends Annotation> annotation = aspect.value();
        if(annotation != null && !annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet() ) {
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for (Class<?> targetClass : targetClassSet) {
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }

    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class, serviceClassSet);
    }

    /*private static void addAspectProxy(Map<Class<?>, List<Class<?>>> proxyMap) throws Exception {
        // 获取切面类（所有继承于 BaseAspect 的类）
        List<Class<?>> aspectProxyClassList = ClassHelper.getClassListBySuper(AspectProxy.class);
        // 添加插件包下所有的切面类
        aspectProxyClassList.addAll(classScanner.getClassListBySuper(FrameworkConstant.PLUGIN_PACKAGE, AspectProxy.class));
        // 排序切面类
        sortAspectProxyClassList(aspectProxyClassList);
        // 遍历切面类
        for (Class<?> aspectProxyClass : aspectProxyClassList) {
            // 判断 Aspect 注解是否存在
            if (aspectProxyClass.isAnnotationPresent(Aspect.class)) {
                // 获取 Aspect 注解
                Aspect aspect = aspectProxyClass.getAnnotation(Aspect.class);
                // 创建目标类列表
                List<Class<?>> targetClassList = createTargetClassList(aspect);
                // 初始化 Proxy Map
                proxyMap.put(aspectProxyClass, targetClassList);
            }
        }
    }*/

    //问题4.CGlib创建的对象会抛出toString异常,未解决
    /*public static void doAopHelper() {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Map map =  BeanHelper.getBeanMap();
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                BeanHelper.setBean(targetClass, proxy );
            }
        } catch (Exception e) {
            LOGGER.error("aop failure", e);
        }
    }*/


}
