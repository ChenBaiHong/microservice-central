package com.baimicro.central.gateway.auth;

import com.baimicro.central.common.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO 401未授权异常处理，转换为JSON
 * version 0.1
 */
@Slf4j
public class JsonAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        return ResponseUtil.responseWriter(exchange, HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }
}
