package com.wudi.td.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.wudi.td.entity.vo.ApiReturnEnum;
import com.wudi.td.entity.vo.ApiReturnType;
import com.wudi.td.util.RedisUtils;
import com.wudi.td.util.RsaSecurityParameter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class LoginController {


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    /**
     * 用户登录
     *
     * @param userName 账号
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ApiReturnType login(@RequestParam(value = "userName", required = true) String userName,
                               @RequestParam(value = "password", required = true)  @RsaSecurityParameter String password,
                               @RequestParam(value="vercode")String vercode) throws JSONException {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String attribute = (String) session.getAttribute(Constants.KAPTCHA_SESSION_CONFIG_KEY);
        if (!attribute.equalsIgnoreCase(vercode)){
            return ApiReturnType.fail(400,"验证码错误!");
        }
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName, password);
        subject.login(usernamePasswordToken);
        if (subject.isAuthenticated()) {
            return ApiReturnType.success();
        } else {
            return ApiReturnType.fail(ApiReturnEnum.LOGIN_ERROR.status(),ApiReturnEnum.LOGIN_ERROR.msg());
        }
    }

    @RequestMapping(value = "/getVerificationCode",method = RequestMethod.GET )
    public void getVerificationCode(HttpServletResponse response) throws IOException {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        //生产验证码字符串并保存到session中
        String createText = defaultKaptcha.createText();
        SecurityUtils.getSubject().getSession().setAttribute(Constants.KAPTCHA_SESSION_CONFIG_KEY, createText);

        //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
        BufferedImage challenge = defaultKaptcha.createImage(createText);
        ImageIO.write(challenge, "jpg", jpegOutputStream);

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();

    }

    @RequestMapping("/unauthorized")
    @ResponseBody
    public ApiReturnType unauthorized(){
        return ApiReturnType.fail(ApiReturnEnum.UNAUTHORIZED_ERROR.status(),ApiReturnEnum.UNAUTHORIZED_ERROR.getMsg());
    }

    @RequestMapping("/toLogin")
    public String tologin() {
        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
