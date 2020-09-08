package com.wudi.td.service;

import com.github.pagehelper.PageInfo;
import com.wudi.td.entity.primary.SysRole;

public interface SysRoleService {

    /**
     * 分页查询
     * @param sysRole
     * @return
     */
    PageInfo pagingQuery(SysRole sysRole, PageInfo pageInfo);

    /**
     * 修改角色信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysRole record);
}
