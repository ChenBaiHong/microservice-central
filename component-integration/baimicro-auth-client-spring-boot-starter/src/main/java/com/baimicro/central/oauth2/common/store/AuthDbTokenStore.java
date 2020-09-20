package com.baimicro.central.oauth2.common.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @className AuthDbTokenStore
 * @Description TODO 认证 数据库 存取 令牌
 * @Author baigle.chen
 * @Date 2019-11-10 10:51
 * @Version 1.0
 */
@ConditionalOnProperty(prefix = "his.cloud.oauth2.token.store", name = "type", havingValue = "db")
// Conditional adj. 有条件的、依...而定...
public class AuthDbTokenStore {

    // 数据源自动装载
    @Autowired
    private DataSource dataSource;

    /**
     * @Author baigle.chen
     * @Description TODO 配置 Java 数据库连接 需要 Token 认证令牌 的 配置Bean类
     * @Date 2019-11-10
     * @Param []
     * @return org.springframework.security.oauth2.provider.token.TokenStore
     **/
    @Bean
    public TokenStore tokenStore(){
        return new JdbcTokenStore(dataSource);
    }
}
