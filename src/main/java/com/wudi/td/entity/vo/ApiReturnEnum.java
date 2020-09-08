package com.wudi.td.entity.vo;

import lombok.Getter;

@Getter
public enum ApiReturnEnum {

   SUCCESS(200,"操作成功！"),
   FAIL(500,"操作失败！"),
    UNAUTHORIZED_ERROR(400,"没有权限!"),
   LOGIN_ERROR(400,"账号或密码错误!"),
    PARAMS_ERROR(400, "参数不合法！"),
    COMMUNI_ERROR(400,"通信异常!"),
    DATA_NOT_FOUND(400, "查询失败！");

    private Integer status;
    private String msg;

    ApiReturnEnum(Integer status,String msg){
        this.status= status;
        this.msg = msg;
    }

    public Integer status(){
        return status;
    }

    public String msg(){
        return msg;
    }

}
