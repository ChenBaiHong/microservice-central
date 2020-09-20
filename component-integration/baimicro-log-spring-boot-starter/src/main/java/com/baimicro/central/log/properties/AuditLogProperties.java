package com.baimicro.central.log.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @program: hospital-cloud-platform
 * @description: 审计日志配置
 * @author: baiHoo.chen
 * @create: 2020-04-07
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "his.cloud.audit-log")
@RefreshScope
public class AuditLogProperties {
    /**
     * 是否开启审计日志
     */
    private Boolean enabled = false;
    /**
     * 日志记录类型(logger/redis/db/es)
     */
    private String logType;
}

