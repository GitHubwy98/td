package com.wudi.td.dao.primary;

import com.wudi.td.entity.primary.SysUserMenu;
import org.apache.ibatis.annotations.Param;

public interface SysUserMenuMapper {
    int deleteByPrimaryKey(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    int insert(SysUserMenu record);

    int insertSelective(SysUserMenu record);
}