package com.baimicro.central.oauth2.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @className SecurityProperties
 * @Description TODO
 * @Author baigle.chen
 * @Date 2019-11-10
 * @Version 1.0
 */
@Getter
@Setter
@ConfigurationProperties("his.cloud.security")
@RefreshScope // 自动化配置需要刷新配置资源
public class SecurityProperties {

    private AuthProperties auth = new AuthProperties();

    private PermitProperties ignore = new PermitProperties();

    private ValidateCodeProperties code = new ValidateCodeProperties();
}
