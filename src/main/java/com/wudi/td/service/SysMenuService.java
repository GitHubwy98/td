package com.wudi.td.service;

import com.wudi.td.entity.primary.SysMenu;
import com.wudi.td.entity.vo.MenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysMenuService {

    /**
     * 查询权限和角色
     * @param userId 用户id
     * @return
     */
    List<Map<String,String>> selectAccessPermissionsRoles(Integer userId);

    /**
     * 获取所有需要拦截的url
     * @return
     */
    List<SysMenu> queryInterceptorPath();

    /**
     * 查询用户菜单
     * @param userId 用户id
     * @return
     */
    List<MenuVo> queryLeftMenu(@Param("userId") Integer userId);

    /**
     * 查询所有菜单
     * @return
     */
    List<MenuVo> queryAll();
}
