package com.baimicro.central.log.config;

import com.baimicro.central.log.properties.AuditLogProperties;
import com.baimicro.central.log.properties.LogDbProperties;
import com.baimicro.central.log.properties.TraceProperties;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 日志自动配置
 * version 0.1
 */
@EnableConfigurationProperties({TraceProperties.class, AuditLogProperties.class})
public class LogAutoConfigure {
    /**
     * 日志数据库配置
     */
    @Configuration
    @ConditionalOnClass(HikariConfig.class)
    @EnableConfigurationProperties(LogDbProperties.class)
    public static class LogDbAutoConfigure {}
}
