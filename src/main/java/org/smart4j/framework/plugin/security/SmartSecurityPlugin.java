package org.smart4j.framework.plugin.security;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;
import org.apache.shiro.web.env.EnvironmentLoaderListener;

/**
 * Smart Security 插件
 */
public class SmartSecurityPlugin implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> handlesTypes, ServletContext servletContext) throws ServletException {
        //设置初始化参数
        servletContext.setInitParameter("shiroConfigLocations", "classpath:smart-security.ini");
        //注册Listener
        servletContext.addListener(EnvironmentLoaderListener.class);
        //注册 Filter
        FilterRegistration.Dynamic smartSecurityFilter = servletContext.addFilter("SmartSecurityFilter", SmartSecurityFilter.class);
        smartSecurityFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}
