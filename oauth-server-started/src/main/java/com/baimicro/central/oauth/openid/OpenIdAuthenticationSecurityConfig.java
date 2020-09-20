package com.baimicro.central.oauth.openid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO openId的相关处理配置
 * version 0.1
 */
@Component
public class OpenIdAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private SocialUserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) {
        //openId provider
        OpenIdAuthenticationProvider provider = new OpenIdAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        http.authenticationProvider(provider);
    }
}
