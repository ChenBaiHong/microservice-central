package com.baimicro.central.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 多租户配置
 * version 0.1
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "his.cloud.tenant")
@RefreshScope
public class TenantProperties {
    /**
     * 是否开启多租户
     */
    private Boolean enable = false;

    /**
     * 配置不进行多租户隔离的表名
     */
    private List<String> ignoreTables = new ArrayList<>();

    /**
     * 配置不进行多租户隔离的sql
     * 需要配置 mapper 的全路径,例如：com.baimicro.central.user.mapper.SysUserMapper.findList
     */
    private List<String> ignoreSqls = new ArrayList<>();
}
