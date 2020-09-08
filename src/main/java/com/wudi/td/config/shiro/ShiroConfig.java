package com.wudi.td.config.shiro;

import com.wudi.td.entity.primary.SysMenu;
import com.wudi.td.service.SysMenuService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    private SysMenuService sysMenuService;

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setUsePrefix(true);
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    /***
     * shiro过滤器
     * anon 无需认证
     * authc 必须认证
     * user 如果使用rememberMe的功能就可以访问
     * perms 必须得到资源权限才能访问
     * role 必须得到角色权限才能访问
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultSecurityManager());
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        //配置需要拦截的路径
        List<SysMenu> sysMenus = sysMenuService.queryInterceptorPath();
        sysMenus.forEach(sysMenu -> {
            String accessPermissions = "perms["+sysMenu.getPermission()+"]";
            filterChainDefinitionMap.put(sysMenu.getUrl(),accessPermissions);
        });
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
        filterChainDefinitionMap.put("/login","anon");
        filterChainDefinitionMap.put("/static/**","anon");
        filterChainDefinitionMap.put("/js/**","anon");
        filterChainDefinitionMap.put("/toObtain","anon");
        filterChainDefinitionMap.put("/getVerificationCode","anon");
        filterChainDefinitionMap.put("/**","authc");
        // 添加自己的过滤器
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 安全管理器
     * @return
     */
    @Bean("defaultSecurityManager")
    public DefaultWebSecurityManager defaultSecurityManager(){
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(customRealm());
        defaultSecurityManager.setSessionManager(sessionManager());
        defaultSecurityManager.setCacheManager(redisCacheManager());
        return defaultSecurityManager;
    }

    /**
     *自定义Realm
     * @return
     */
    @Bean
    public CustomRealm customRealm(){
        CustomRealm customRealm = new CustomRealm();
        customRealm.setAuthorizationCachingEnabled(true);
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }

    /**
     * 自定义密码比对
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashed = new HashedCredentialsMatcher();
        hashed.setHashAlgorithmName("MD5");
        hashed.setStoredCredentialsHexEncoded(true);
        hashed.setHashIterations(3);
        return hashed;
    }

    /**
     * 使用redis 作为shiro的缓存
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //缓存过期时间
        redisCacheManager.setExpire(5*60*1000);
        redisCacheManager.setPrincipalIdFieldName("userId");
        return redisCacheManager;
    }

    /**
     * 注入属性
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisManager redisManager(){
        return new RedisManager();
    }

    @Bean
    public SessionManager sessionManager(){
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionDAO(redisSessionDAO());
        //取消url 后面的 JSESSIONID
        defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false);
        //全局会话超时时间（单位毫秒），默认30分钟
        defaultWebSessionManager.setGlobalSessionTimeout(5*60*1000);
        //设置session失效的扫描时间,（单位毫秒） 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        defaultWebSessionManager.setSessionValidationInterval(3600000);
        // 删除无效session
        defaultWebSessionManager.setDeleteInvalidSessions(true);
        defaultWebSessionManager.setSessionIdCookie(sessionIdCookie());
        return defaultWebSessionManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        // 过期时间 30*60*1000 单位秒
        redisSessionDAO.setExpire(5*60);
        return redisSessionDAO;
    }

    /**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     * 默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid
     * @return
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }


    /**
     * 记住密码 自动登录
     * 1.配置cookie
     * 2.添加到cookie管理对象
     * 3.添加到shiro安全管理器
     */

//
//    /**
//     * ookie管理对象
//     * 生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
//     * @return
//     */
//    public CookieRememberMeManager rememberMeManager(){
//        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
//        rememberMeManager.setCookie(rememberCookie());
//        return  rememberMeManager;
//    }
}