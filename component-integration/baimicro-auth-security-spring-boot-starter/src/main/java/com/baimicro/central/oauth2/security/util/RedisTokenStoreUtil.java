package com.baimicro.central.oauth2.security.util;

import com.baimicro.central.common.constant.SecurityConstants;
import com.baimicro.central.redis.template.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.stereotype.Component;

/**
 * @className RedisTokenStoreUtil
 * @Description TODO 优化来自 Spring Security 的 RedisTokenStore
 * 优化自Spring Security的RedisTokenStore
 * 1. 支持redis所有集群模式包括cluster模式
 * 2. 使用pipeline减少连接次数，提升性能
 * 3. 自动续签token（可配置是否开启）
 * @Author baigle.chen
 * @Date 2019-11-10 15:12
 * @Version 1.0
 */
@Component
@SuppressWarnings("all")
public class RedisTokenStoreUtil {

    @Autowired
    private RedisRepository redisRepository;

    private static final String ACCESS = "access:";

    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    private String prefix = "";


    private byte[] serializeKey(String object) {
        return serialize(prefix + object);
    }

    private OAuth2AccessToken deserializeAccessToken(byte[] bytes) {
        return serializationStrategy.deserialize(bytes, OAuth2AccessToken.class);
    }

    private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
        return serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }

    private byte[] serialize(String string) {
        return serializationStrategy.serialize(string);
    }


    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        OAuth2Authentication auth2Authentication = readAuthentication(token.getValue());

        return auth2Authentication;
    }


    public OAuth2Authentication readAuthentication(String token) {
        byte[] bytes;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(serializeKey(SecurityConstants.REDIS_TOKEN_AUTH + token));
        } finally {
            conn.close();
        }
        return deserializeAuthentication(bytes);
    }


    public OAuth2AccessToken readAccessToken(String tokenValue) {
        byte[] key = serializeKey(ACCESS + tokenValue);
        byte[] bytes;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(key);
        } finally {
            conn.close();
        }
        return deserializeAccessToken(bytes);
    }

    private RedisConnection getConnection() {
        return redisRepository.getConnectionFactory().getConnection();
    }


    private static String getApprovalKey(OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication() == null ? ""
                : authentication.getUserAuthentication().getName();
        return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
    }

    private static String getApprovalKey(String clientId, String userName) {
        return clientId + (userName == null ? "" : ":" + userName);
    }
}
