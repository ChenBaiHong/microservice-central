package com.baimicro.central.oauth2.common.token;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/23
 * @Description: TODO 增加租户id，解决不同租户单点登录时角色没变化
 * version 0.1
 */
public class TenantUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    /**
     * 租户id
     */
    @Getter
    private final String clientId;

    public TenantUsernamePasswordAuthenticationToken(Object principal, Object credentials, String clientId) {
        super(principal, credentials);
        this.clientId = clientId;
    }

    public TenantUsernamePasswordAuthenticationToken(Object principal, Object credentials,
                                                     Collection<? extends GrantedAuthority> authorities, String clientId) {
        super(principal, credentials, authorities);
        this.clientId = clientId;
    }
}
