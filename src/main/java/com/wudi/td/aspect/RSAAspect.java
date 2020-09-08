package com.wudi.td.aspect;

import com.wudi.td.util.RSAUtils;
import com.wudi.td.util.RsaSecurityParameter;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Aspect
@Component
public class RSAAspect {

    @Value("${RSA.privateKey}")
    private String privateKey;

    @Pointcut(value = "execution(* com.wudi.td.controller..*.*(..))")
    public void controllerMethodPointcut() {
    }


    @Around("controllerMethodPointcut()")
    public Object Interceptor(ProceedingJoinPoint pjPoint) throws Throwable {
        //获取方法参数
        Object[] args = pjPoint.getArgs();
        //获取私钥
        MethodSignature methodSignature = (MethodSignature) pjPoint.getSignature();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
        if (parameterAnnotations.length > 0) {
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                int paramIndex = ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof RsaSecurityParameter) {
                        Object paramValue = args[paramIndex];
                        String decrypt = RSAUtils.decryptDataPrivateKeyOnJava(paramValue.toString(), privateKey);
                        args[paramIndex] = decrypt;
                    }
                }
            }
            return pjPoint.proceed(args);
        }
        return pjPoint.proceed();
    }
}
