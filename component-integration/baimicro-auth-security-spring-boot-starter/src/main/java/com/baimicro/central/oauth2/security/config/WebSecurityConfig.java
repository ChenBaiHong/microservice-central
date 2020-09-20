package com.baimicro.central.oauth2.security.config;

import com.baimicro.central.oauth2.security.properties.UrlPermissionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/15
 * @Description: TODO 安全单点登陆
 * version 0.1
 */
@Order(1)
@Configuration
@EnableConfigurationProperties(UrlPermissionProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UrlPermissionProperties properties;

    @Value("${security.oauth2.sso.login-path:}")
    private String loginPath;
    @Override
    protected void configure(HttpSecurity https) throws Exception {
        // 禁止跨站请求伪造
        https.csrf().disable()
                .authorizeRequests()                         // 1. 授权网络请求
//                .antMatchers(properties.getIncludeUrls())  // 1.1 匹配包含的请求地址
//                .authenticated()                           // 1.2 开启认证
                .anyRequest()                                // 2. 其他任意请求
                .permitAll();                                 // 2.1 直接放行
    }
}
