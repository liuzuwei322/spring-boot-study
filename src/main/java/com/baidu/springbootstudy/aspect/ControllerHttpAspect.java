package com.baidu.springbootstudy.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class ControllerHttpAspect {
    private static final Logger logger = LoggerFactory.getLogger(ControllerHttpAspect.class);

    @Pointcut("execution(public * com.baidu.springbootstudy.controller..*(..))")
    public void log() {}

    @Before("log()")
    public void before (JoinPoint joinPoint) {
        String info = "\n" + "服务处理开始" + "\n";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        if (request == null) {
            return;
        }
        info += "URL        : " + request.getRequestURL() + "\n";
        info += "Method     : " + request.getMethod() + "\n";
        info += "IP         : " + request.getRemoteAddr() + "\n";
        info += "参数       : " + JSON.toJSON(joinPoint.getArgs()) + "\n";
        logger.info(info);
    }
}
