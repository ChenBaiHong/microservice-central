package com.baimicro.central.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 密码配置
 * version 0.1
 */
public class DefaultPasswordConfig {

    /**
     * 装配BCryptPasswordEncoder用户密码的匹配
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder()	{
        return new BCryptPasswordEncoder();
    }
}
