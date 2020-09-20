package com.baimicro.central.oauth2.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16 19:16
 * @Description: TODO  url权限配置
 * version 0.1
 */
@Getter
@Setter
@ConfigurationProperties("his.cloud.security")
@RefreshScope // 自动化配置需要刷新配置资源
public class UrlPermissionProperties {

    /**
     * 配置只进行登录认证，进行url权限认证的api
     */
    private String[] includeUrls = {};
}

