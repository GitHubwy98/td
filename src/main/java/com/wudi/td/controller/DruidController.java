package com.wudi.td.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DruidController{

    @RequestMapping("system/toDruidLogin")
    public String toDruidLogin(){
        return "/system/druidLogin";
    }
}
