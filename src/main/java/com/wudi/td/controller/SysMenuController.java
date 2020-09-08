package com.wudi.td.controller;

import com.wudi.td.entity.primary.SysUser;
import com.wudi.td.entity.vo.ApiReturnType;
import com.wudi.td.entity.vo.MenuVo;
import com.wudi.td.service.SysMenuService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @RequestMapping("/se")
    public int se(){
        List maps = sysMenuService.selectAccessPermissionsRoles(61950);
        return 0;
    }

    /**
     * 获取用户左侧菜单
     * @return
     */
    @RequestMapping(value = "/getLeftMenu",method = RequestMethod.GET)
    public ApiReturnType getLeftMenu(){
        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        List<MenuVo> menuVoList = sysMenuService.queryLeftMenu(sysUser.getUserId());
        return ApiReturnType.success(menuVoList);
    }

    @GetMapping(value="/system/menu/query")
    public ApiReturnType query(){
        return null;
    }
}