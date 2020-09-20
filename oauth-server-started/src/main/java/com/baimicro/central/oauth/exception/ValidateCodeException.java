package com.baimicro.central.oauth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO 验证码异常
 * version 0.1
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
