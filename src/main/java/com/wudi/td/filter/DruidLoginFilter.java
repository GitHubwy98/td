package com.wudi.td.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

@Slf4j
@WebFilter(filterName = "druidLogin",urlPatterns = "/toDruidLogin")
public class DruidLoginFilter implements Filter {

    private String loginUsername = "admin";

    private String loginPassword = "123456";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("DruidLoginFilter初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        String requestUrl = request.getRequestURL().toString();
        if (requestUrl.contains("toDruidLogin")){
            String queryString = "loginUsername="+loginUsername+"&loginPassword="+loginPassword;
            // 获取完整路径
            StringBuffer url = new StringBuffer(requestUrl);
            // 获取路径加上项目名称
            String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).append("/").toString();
            /** 构造新地址，其实就是druid的登录地址 */
            URL newUrl = new URL(tempContextUrl + "druid/submitLogin?" + queryString);
            response.setStatus(307);
            response.setHeader("Location", newUrl.toString());
            response.setHeader("Connection", "close");
        }
    }
}
