package com.wudi.td.config;


import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha defaultKaptcha(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        //是否使用边框
        properties.setProperty("kaptcha.border", "no");
        //边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        //验证码字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "red");
        //验证码图片的宽度
        properties.setProperty("kaptcha.image.width", "129");
        //验证码图片的高度
        properties.setProperty("kaptcha.image.height", "38");
        //验证码字体的大小
        properties.setProperty("kaptcha.textproducer.font.size", "38");
        //验证码保存在session的key
        properties.setProperty("kaptcha.session.key", Constants.KAPTCHA_SESSION_CONFIG_KEY);
        //验证码输出的字符长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        //验证码的字体设置
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        properties.setProperty("kaptcha.background.clear.from","white");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}