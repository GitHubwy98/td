package com.wudi.td.common;

public class CommonConstant {

    /**
     * token的key
     */
    public static String ACCESS_TOKEN = "Access-Token";


    public static final String PREFIX_USER_TOKEN = "PREFIX_USER_TOKEN_";

    public static final String LOGIN_SALT = "storyweb-bp";
    //request请求头属性
    public static final String REQUEST_AUTH_HEADER="Authorization";

    //JWT-account
    public static final String ACCOUNT = "account";

    //Shiro redis 前缀
    public static final String PREFIX_SHIRO_CACHE = "storyweb-bp:cache:";

    //redis-key-前缀-shiro:refresh_token
    public final static String PREFIX_SHIRO_REFRESH_TOKEN = "storyweb-bp:refresh_token:";

    //JWT-currentTimeMillis
    public final static String CURRENT_TIME_MILLIS = "currentTimeMillis";

    public final static String RSA_PRIVATEKEY="rsaPrivateKey";
}
