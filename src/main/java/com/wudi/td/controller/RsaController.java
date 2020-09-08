package com.wudi.td.controller;

import com.wudi.td.common.CommonConstant;
import com.wudi.td.entity.vo.ApiReturnType;
import com.wudi.td.util.RSAUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RsaController {

    @GetMapping("/toObtain")
    public ApiReturnType toObtain() throws Exception {
        Map<String, Object> map = RSAUtils.initKey();
        SecurityUtils.getSubject().getSession().setAttribute(CommonConstant.RSA_PRIVATEKEY,RSAUtils.getPrivateKeyStr(map));
        return ApiReturnType.success(RSAUtils.getPublicKeyStr(map));
    }
}
