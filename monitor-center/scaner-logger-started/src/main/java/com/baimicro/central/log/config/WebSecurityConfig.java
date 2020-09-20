package com.baimicro.central.log.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/15
 * @Description: TODO 安全单点登陆
 * version 0.1
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



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
