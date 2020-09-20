package com.baimicro.central.log.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 日志链路追踪配置
 * version 0.1
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "his.cloud.trace")
@RefreshScope
public class TraceProperties {

    /**
     * 是否开启日志链路追踪
     */
    private Boolean enable = false;
}
