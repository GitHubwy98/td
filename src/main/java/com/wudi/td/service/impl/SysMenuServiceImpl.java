package com.wudi.td.service.impl;

import com.wudi.td.dao.primary.SysMenuMapper;
import com.wudi.td.entity.primary.SysMenu;
import com.wudi.td.entity.vo.MenuVo;
import com.wudi.td.service.SysMenuService;
import com.wudi.td.util.MenuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<Map<String,String>> selectAccessPermissionsRoles(Integer userId) {
        List<Map<String,String>> maps = sysMenuMapper.selectAccessPermissionsRoles(userId);
        if (!CollectionUtils.isEmpty(maps)){
            return maps;
        }
        return null;
    }

    @Override
    public List<SysMenu> queryInterceptorPath() {
        List<SysMenu> sysMenus = sysMenuMapper.queryInterceptorPath();
        if (sysMenus!=null){
            return sysMenus;
        }
        return null;
    }

    @Override
    public List<MenuVo> queryLeftMenu(Integer userId) {
        List<SysMenu> menus = sysMenuMapper.queryLeftMenu(userId);
        if (!CollectionUtils.isEmpty(menus)){
            List<MenuVo> menuVoList = MenuUtil.previousPage(menus);
            return menuVoList;
        }
       return null;
    }

    @Override
    public List<MenuVo> queryAll() {
        List<SysMenu> menus = sysMenuMapper.queryLeftMenu(null);


        return null;
    }
}