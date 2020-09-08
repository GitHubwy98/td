package com.wudi.td.dao.primary;

import com.wudi.td.entity.primary.SysRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleMapper {
    int deleteByPrimaryKey(Long roleId);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     * 分页查询
     * @param sysRole
     * @return
     */
    List<SysRole> pagingQuery(SysRole sysRole);
}