package com.baimicro.central.common.config;

import com.baimicro.central.common.feign.UserService;
import com.baimicro.central.common.resolver.ClientArgumentResolver;
import com.baimicro.central.common.resolver.TokenArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 登陆公共配置类
 * version 0.1
 */
public class LoginArgResolverConfig implements WebMvcConfigurer {


    @Lazy
    @Autowired
    private UserService userService;

    /**
     * Token参数解析
     *
     * @param argumentResolvers 解析类
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //注入用户信息
        argumentResolvers.add(new TokenArgumentResolver(userService));
        //注入应用信息
        argumentResolvers.add(new ClientArgumentResolver());
    }
}
