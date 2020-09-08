package com.wudi.td.controller;

import com.github.pagehelper.PageInfo;
import com.wudi.td.entity.primary.SysRole;
import com.wudi.td.entity.vo.ApiReturnEnum;
import com.wudi.td.entity.vo.ApiReturnType;
import com.wudi.td.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("system/role")
    public String toRoleManagement(){
        return "system/roleManagement";
    }

    @GetMapping("system/role/query")
    @ResponseBody
    public ApiReturnType query(SysRole sysRole, PageInfo pageInfo){
        PageInfo page= sysRoleService.pagingQuery(sysRole, pageInfo);
        return ApiReturnType.success(page);

    }

    @PutMapping("system/role/update")
    @ResponseBody
    public ApiReturnType update(@Validated  SysRole sysRole){
        int i = sysRoleService.updateByPrimaryKeySelective(sysRole);
        if (i!=0){
            return ApiReturnType.success();
        }else {
            return ApiReturnType.fail(ApiReturnEnum.FAIL);
        }
    }
}
