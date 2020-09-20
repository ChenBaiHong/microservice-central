package com.baimicro.central.oauth.tenant;

import com.baimicro.central.oauth.service.MyUserDetailsService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/24
 * @Description: TODO 租户鉴权安全配置
 * version 0.1
 */
@Component
public class TenantAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private MyUserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    public TenantAuthenticationSecurityConfig(MyUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(HttpSecurity http) {
        TenantAuthenticationProvider provider = new TenantAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        http.authenticationProvider(provider);
    }
}
