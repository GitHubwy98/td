package com.wudi.td.service;

import com.wudi.td.entity.primary.SysUser;


public interface SysUserService {

    int insert(SysUser record);

    SysUser selectByUserName(String userName);

}
