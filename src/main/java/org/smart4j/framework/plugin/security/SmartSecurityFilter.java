package org.smart4j.framework.plugin.security;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.CachingSecurityManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.smart4j.framework.plugin.security.realm.SmartCustomRealm;
import org.smart4j.framework.plugin.security.realm.SmartJdbcRealm;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 安全过滤器
 */
public class SmartSecurityFilter extends ShiroFilter {
    @Override
    public void init() throws Exception {
        super.init();
        WebSecurityManager webSecurityManager = super.getSecurityManager();
        //设置 Realm, 同时支持多个Realm,并按照先后顺序用逗号问个
        setRealms(webSecurityManager);
        //设置 Cache, 用于减少数据库查询次数,降低I/O访问
        setCache(webSecurityManager);
    }
    private void setRealms(WebSecurityManager webSecurityManager) {
        //读取 smart.plugin.security.realms 配置项
        String securityRealms = SecurityConfig.getRealms();
        if (securityRealms != null) {
            //根据逗号拆分
            String[] securityRealmArray = securityRealms.split(",");
            if (securityRealmArray.length > 0) {
                //使 Realm 具备唯一性与顺序性
                Set<Realm> realms = new LinkedHashSet<>();
                for (String securityRealm : securityRealmArray) {
                    if (securityRealm.equalsIgnoreCase(SecurityConstant.REALMS_JDBC)) {
                        //添加基于JDBC的Realm, 需配置相关SQL查询语句
                        addJdbcRealm(realms);
                    } else if (securityRealm.equalsIgnoreCase(SecurityConstant.REALMS_CUSTOM)) {
                        //添加基于定制化的 Rralm, 需实现 SmartSecurity 接口
                        addCustomRealm(realms);
                    }
                }
                RealmSecurityManager realmSecurityManager = (RealmSecurityManager) webSecurityManager;
                realmSecurityManager.setRealms(realms);//设置Realm
            }
        }
    }

    private void addJdbcRealm(Set<Realm> realms) {
        // 添加自己实现的基于 JDBC 的 Ream
        SmartJdbcRealm smartJdbcRealm = new SmartJdbcRealm();
        realms.add(smartJdbcRealm);
    }

    private void addCustomRealm(Set<Realm> realms) {
        //读取 smart.plugin.security.custom.class 配置项
        SmartSecurity smartSecurity = SecurityConfig.getSmartSecurity();
        //添加自己实现的Realm
        SmartCustomRealm smartCustomRealm = new SmartCustomRealm(smartSecurity);
        realms.add(smartCustomRealm);
    }


    private void setCache(WebSecurityManager webSecurityManager) {
        // 读取 smart.plugin.security.cache 配置项
        if (SecurityConfig.isCacheable()) {
            CachingSecurityManager cachingSecurityManager = (CachingSecurityManager) webSecurityManager;
            //使用基于内存的 CacheManager
            CacheManager cacheManager = new MemoryConstrainedCacheManager();
            cachingSecurityManager.setCacheManager(cacheManager);
        }

    }
}
