package com.wudi.td.dao.primary;

import com.wudi.td.entity.primary.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysMenuMapper {
    int deleteByPrimaryKey(Long menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    /**
     * 查询权限和角色
     * @param userId 用户id
     * @return
     */
    List<Map<String,String>> selectAccessPermissionsRoles(@Param("userId") Integer userId);

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
    List<SysMenu> queryLeftMenu(@Param("userId") Integer userId);
}