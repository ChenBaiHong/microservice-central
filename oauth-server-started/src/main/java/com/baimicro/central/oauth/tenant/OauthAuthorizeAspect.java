package com.baimicro.central.oauth.tenant;

import com.baimicro.central.common.context.TenantContextHolder;
import com.baimicro.central.common.feign.UserService;
import com.baimicro.central.common.model.LoginAppUser;
import com.baimicro.central.oauth2.common.token.TenantUsernamePasswordAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/24
 * @Description: TODO
 * /oauth/authorize 拦截器
 * 解决不同租户单点登录时角色没变化
 * version 0.1
 */
@Slf4j
@Component
@Aspect
public class OauthAuthorizeAspect {
    @Autowired
    private UserService userService;

    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.authorize(..))")
    public Object doAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Map<String, String> parameters = (Map<String, String>) args[1];
        Principal principal = (Principal) args[3];
        if (principal instanceof TenantUsernamePasswordAuthenticationToken) {
            TenantUsernamePasswordAuthenticationToken tenantToken = (TenantUsernamePasswordAuthenticationToken)principal;
            String clientId = tenantToken.getClientId();
            String requestClientId = parameters.get(OAuth2Utils.CLIENT_ID);
            //判断是否不同租户单点登录
            if (!requestClientId.equals(clientId)) {
                try {
                    TenantContextHolder.setTenant(requestClientId);
                    //重新查询对应该租户的角色等信息
                    LoginAppUser user = userService.findByUsername(tenantToken.getName());
                    tenantToken = new TenantUsernamePasswordAuthenticationToken(user, tenantToken.getCredentials(), user.getAuthorities(), requestClientId);
                    args[3] = tenantToken;
                } finally {
                    TenantContextHolder.clear();
                }
            }
        }
        return joinPoint.proceed(args);
    }
}
