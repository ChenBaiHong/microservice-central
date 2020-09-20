package com.baimicro.central.gateway.auth;

import com.baimicro.central.common.model.AppPerm;
import com.baimicro.central.gateway.feign.PermService;
import com.baimicro.central.oauth2.common.service.impl.DefaultPermissionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO  url权限认证
 * version 0.1
 */
@Slf4j
@Component
public class PermissionAuthManager extends DefaultPermissionServiceImpl implements ReactiveAuthorizationManager<AuthorizationContext> {
    @Resource
    private PermService permService;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        return authentication.map(auth -> {
            ServerWebExchange exchange = authorizationContext.getExchange();
            ServerHttpRequest request = exchange.getRequest();
            boolean isPermission = super.hasPermission(auth, request.getMethodValue(), request.getURI().getPath());
            return new AuthorizationDecision(isPermission);
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }

    @Override
    public List<AppPerm> findPermByRoleCodes(String roleCodes) {
        return permService.findByRoleCodes(roleCodes);
    }
}
