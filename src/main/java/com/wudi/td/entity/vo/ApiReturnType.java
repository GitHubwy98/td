package com.wudi.td.entity.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ApiReturnType implements Serializable {

    private Integer status;

    private String msg;

    private Object data;

    public ApiReturnType() { }

    public ApiReturnType(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static ApiReturnType success() {
        return  apiReturnType(ApiReturnEnum.SUCCESS.getStatus(),ApiReturnEnum.SUCCESS.getMsg(),null);
    }

    public static ApiReturnType success(Object data) {
        return  apiReturnType(ApiReturnEnum.SUCCESS.getStatus(),ApiReturnEnum.SUCCESS.getMsg(),data);
    }

    public static ApiReturnType success(String msg,Object data) {
        return  apiReturnType(ApiReturnEnum.SUCCESS.status(),msg,data);
    }

    public static ApiReturnType fail(ApiReturnEnum apiReturnEnum) {
        return apiReturnType(apiReturnEnum.status(), apiReturnEnum.getMsg(), null);
    }

    public static ApiReturnType fail(Integer status, String msg) {
        return apiReturnType(status, msg, null);
    }

    public static ApiReturnType fail(Integer status, String msg, Object data) {
        return apiReturnType(status, msg, data);
    }


    private static ApiReturnType apiReturnType(Integer status, String msg, Object data) {
        ApiReturnType resultData = new ApiReturnType();
        resultData.setStatus(status);
        resultData.setMsg(msg);
        resultData.setData(data);
        return resultData;
    }

}
