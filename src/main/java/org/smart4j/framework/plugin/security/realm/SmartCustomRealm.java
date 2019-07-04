package org.smart4j.framework.plugin.security.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.smart4j.framework.plugin.security.SecurityConstant;
import org.smart4j.framework.plugin.security.SmartSecurity;
import org.smart4j.framework.plugin.security.password.Md5CredentialsMatcher;

import java.util.HashSet;
import java.util.Set;

/**
 * 基于 Smart 的 自定义 Realm(需要实现 SmartSecurity 接口)
 */
public class SmartCustomRealm extends AuthorizingRealm {

    private final SmartSecurity smartSecurity;

    public SmartCustomRealm(SmartSecurity smartSecurity) {
        this.smartSecurity = smartSecurity;
        super.setName(SecurityConstant.REALMS_CUSTOM);
        //使用MD5
        super.setCredentialsMatcher(new Md5CredentialsMatcher());
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token == null) {
            throw new AuthenticationException("parameter token is null");
        }
        //通过 AuthenticationToke 对象获取从表单中提交的用户名
        String username = ((UsernamePasswordToken) token).getUsername();
        //通过 SmartSecurity 接口并根据用户名过去数据库中存放的密码
        String password = smartSecurity.getPassword(username);
        //将用户名密码放入AuthenticationInfo 对象中, 便于后续的认证操作
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo();
        authenticationInfo.setPrincipals(new SimplePrincipalCollection(username, super.getName()));
        return authenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if (principalCollection == null) {
            throw new AuthenticationException("parameter principals is null");
        }
        //获取已认证的用户的用户名
        String username = (String) super.getAvailablePrincipal(principalCollection);
        //通过smartSecurity 接口并根据用户名获取角色名集合
        Set<String> roleNameSet = smartSecurity.getRoleNameSet(username);
        //通过SmartSecurity 接口并根据角色名获取与其对应的权限名集合
        Set<String> permissionNameSet = new HashSet<>();
        if (roleNameSet != null && roleNameSet.size() > 0) {
            for (String rolename : roleNameSet) {
                Set<String> currentPermissionNameSet = smartSecurity.getPermissionNameSet(rolename);
                permissionNameSet.addAll(currentPermissionNameSet);
            }
        }
        //将角色名集合与权限名集合放入 AuthorizationInfo 对象中便于后续操作
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roleNameSet);
        authorizationInfo.setStringPermissions(permissionNameSet);
        return authorizationInfo;
    }
}
