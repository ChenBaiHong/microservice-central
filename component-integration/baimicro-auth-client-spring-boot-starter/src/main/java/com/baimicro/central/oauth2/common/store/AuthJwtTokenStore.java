package com.baimicro.central.oauth2.common.store;

import com.baimicro.central.oauth2.common.converter.CustomUserAuthenticationConverter;
import com.baimicro.central.common.model.AppUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

/**
 * @className AuthJwtTokenStore
 * @Description TODO 认证服务器使用 JWT 、RAS 非对称加密令牌
 * @Author baigle.chen
 * @Date 2019-11-10 15:27
 * @Version 1.0
 */
@ConditionalOnProperty(prefix = "his.cloud.oauth2.token.store", name = "type", havingValue = "authJwt")
public class AuthJwtTokenStore {

    @Bean("keyProp")
    public KeyProperties keyProperties() {
        return new KeyProperties();
    }

    @Resource(name = "keyProp")
    private KeyProperties keyProperties;

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    @Order(2)
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory
                (keyProperties.getKeyStore().getLocation(), keyProperties.getKeyStore().getSecret().toCharArray())
                .getKeyPair(keyProperties.getKeyStore().getAlias());
        converter.setKeyPair(keyPair);
        DefaultAccessTokenConverter tokenConverter = (DefaultAccessTokenConverter)converter.getAccessTokenConverter();
        tokenConverter.setUserTokenConverter(new CustomUserAuthenticationConverter());
        return converter;
    }

    /**
     * jwt 生成token 定制化处理
     * 添加一些额外的用户信息到token里面
     *
     * @return TokenEnhancer
     */
    @Bean
    @Order(1)
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>(1);
            Object principal = authentication.getPrincipal();
            //增加id参数
            if (principal instanceof AppUser) {
                AppUser user = (AppUser)principal;
                additionalInfo.put("id", user.getId());
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }
}
