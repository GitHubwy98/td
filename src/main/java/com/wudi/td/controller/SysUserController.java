package com.wudi.td.controller;

import com.wudi.td.entity.primary.SysUser;
import com.wudi.td.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("system")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/insert")
    public int insertSys(String user){
        SysUser sysUser = new SysUser();
        sysUser.setUserName(user);
        int i = sysUserService.insert(sysUser);
        return i;
    }
}
