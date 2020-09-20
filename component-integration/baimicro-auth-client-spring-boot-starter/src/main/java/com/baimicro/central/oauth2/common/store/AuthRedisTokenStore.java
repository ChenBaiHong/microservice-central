package com.baimicro.central.oauth2.common.store;

import com.baimicro.central.oauth2.common.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @className AuthRedisTokenStore
 * @Description TODO 认证服务器使用 Redis 存取令牌。注意：需要配置 Redis 参数
 * @Author baigle.chen
 * @Date 2019-11-10
 * @Version 1.0
 */
// Redis认证服务器配置类，使用 Redis 存取令牌。注意：需要配置 Redis 参数
@Configuration
@ConditionalOnProperty(prefix = "his.cloud.oauth2.token.store", name = "type", havingValue = "redis",
        // 如果未设置属性，则指定条件是否应匹配。默认为 false, 我们这里设置为 true, 该 RedisTokenConfig 必须配置
        matchIfMissing = true)
public class AuthRedisTokenStore {

    @Bean
    public TokenStore tokenStore(RedisConnectionFactory connectionFactory, SecurityProperties securityProperties) {
        return new CustomRedisTokenStore(connectionFactory, securityProperties);
    }
}
