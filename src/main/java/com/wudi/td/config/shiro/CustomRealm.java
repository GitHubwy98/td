package com.wudi.td.config.shiro;


import com.wudi.td.entity.primary.SysUser;
import com.wudi.td.service.SysMenuService;
import com.wudi.td.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;


@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        List<Map<String,String>> maps = sysMenuService.selectAccessPermissionsRoles(sysUser.getUserId());
        if (!CollectionUtils.isEmpty(maps)){
            SimpleAuthorizationInfo authorizationInfo  = new SimpleAuthorizationInfo();
            maps.forEach(map -> {
                authorizationInfo.addRole(map.get("role_key"));
                authorizationInfo.addStringPermission(map.get("permission"));
            });
            return authorizationInfo;
        }
        return null;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        String  username  = (String) authenticationToken.getPrincipal();
        SysUser sysUser = sysUserService.selectByUserName(username);
        if (sysUser!=null){
            return new SimpleAuthenticationInfo(sysUser,sysUser.getPassword(), ByteSource.Util.bytes(sysUser.getUserName()),this.getName());
        }
        return null;
    }

    @Override
    public AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
        return super.getAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

}