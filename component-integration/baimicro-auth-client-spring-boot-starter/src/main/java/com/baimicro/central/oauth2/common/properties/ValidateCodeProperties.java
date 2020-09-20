package com.baimicro.central.oauth2.common.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @className ValidateCodeProperties
 * @Description TODO 验证码配置
 * @Author baigle.chen
 * @Date 2019-11-10 18:10
 * @Version 1.0
 */
@Setter
@Getter
public class ValidateCodeProperties {

    /**
     * 设置认证通时不需要验证码的clientId
     */
    private String[] ignoreClientCode = {};
}
