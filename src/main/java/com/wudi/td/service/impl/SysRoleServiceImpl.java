package com.wudi.td.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wudi.td.config.shiro.CustomRealm;
import com.wudi.td.dao.primary.SysRoleMapper;
import com.wudi.td.entity.primary.SysRole;
import com.wudi.td.service.SysRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public PageInfo pagingQuery(SysRole sysRole, PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getPageNum(),pageInfo.getPageSize(),true);
        List<SysRole> sysRoles = sysRoleMapper.pagingQuery(sysRole);
        if (!CollectionUtils.isEmpty(sysRoles)){
            return new PageInfo(sysRoles);
        }
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(SysRole record) {
        CustomRealm customRealm = new CustomRealm();
        Subject subject = SecurityUtils.getSubject();
        customRealm.clearCachedAuthorizationInfo(subject.getPrincipals());
        int i = sysRoleMapper.updateByPrimaryKeySelective(record);
        return i;
    }
}
