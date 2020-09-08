package com.wudi.td.handle;

import com.wudi.td.entity.vo.ApiReturnEnum;
import com.wudi.td.entity.vo.ApiReturnType;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice //(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiReturnType runtimeExceptionHandler(Exception e){
        log.error("请求出现异常,异常信息为: {}",e.getMessage());
        return ApiReturnType.fail(ApiReturnEnum.DATA_NOT_FOUND.status(),ApiReturnEnum.DATA_NOT_FOUND.msg());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiReturnType httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e){
        log.error("请求类型出现异常，异常信息为：{}",e.getMessage());
        return ApiReturnType.fail(ApiReturnEnum.PARAMS_ERROR.status(),ApiReturnEnum.PARAMS_ERROR.msg());
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    public ApiReturnType  incorrectCredentialsException(){
        return ApiReturnType.fail(ApiReturnEnum.LOGIN_ERROR.status(),ApiReturnEnum.LOGIN_ERROR.msg());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ApiReturnType unauthorizedException(Exception e){
        log.error("异常信息为：{}",e.getMessage());
        return ApiReturnType.fail(ApiReturnEnum.UNAUTHORIZED_ERROR.status(),ApiReturnEnum.UNAUTHORIZED_ERROR.getMsg());
    }
}