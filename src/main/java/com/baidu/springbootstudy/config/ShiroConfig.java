package com.baidu.springbootstudy.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean (SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置secruityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 登陆的url
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登陆成功后的url, 前后端分离的项目，这里设置了没有用
        // shiroFilterFactoryBean.setSuccessUrl("/say");
        // 没有权限后跳转的url
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        // 设置filterChain, 静态资源不拦截
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("/css/**", "anon");
        linkedHashMap.put("/js/**", "anon");
        linkedHashMap.put("/fonts/**", "anon");
        linkedHashMap.put("/img/**", "anon");
        // 配置退出过滤器，其中具体的退出代码Shiro已经替我们实现了
        linkedHashMap.put("/logout", "logout");
        linkedHashMap.put("/", "anon");
        // 除上以外所有url都必须认证通过才可以访问，未通过认证自动访问LoginUrl
        // user指的是用户认证通过或者配置了Remember Me记住用户登录状态后可访问
        linkedHashMap.put("/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(linkedHashMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager () {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }
    /**
     * cookie对象
     * @return
     */
    public SimpleCookie rememberMeCookie() {
        // 设置cookie名称，对应login.html页面的<input type="checkbox" name="rememberMe"/>
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // 设置cookie的过期时间，单位为秒，这里为一天
        cookie.setMaxAge(86400);
        return cookie;
    }

    @Bean
    public ShiroRealm shiroRealm () {
        ShiroRealm shiroRealm = new ShiroRealm();
        return shiroRealm;
    }

    // 开启Shiro的注解功能
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor (SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
