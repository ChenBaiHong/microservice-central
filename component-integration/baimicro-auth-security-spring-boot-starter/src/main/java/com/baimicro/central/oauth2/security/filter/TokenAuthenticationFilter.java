package com.baimicro.central.oauth2.security.filter;

import com.baimicro.central.oauth2.security.util.RedisTokenStoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/15
 * @Description: TODO
 * version 0.1
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTokenStoreUtil redisTokenStoreUtil;

    /**
     * 分布式权限 Token 传递
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletRequest.getHeader("Authorization");
        // 去掉 "Bearer " 前缀
        if (authorization!=null && authorization.length()>6){
            authorization = authorization.substring(6,authorization.length()).trim();
            OAuth2Authentication auth2Authentication = redisTokenStoreUtil.readAuthentication(authorization);
            if (auth2Authentication != null
                    && auth2Authentication.getPrincipal() != null) {
                SecurityContextHolder.getContext().setAuthentication(auth2Authentication);
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
