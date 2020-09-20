package com.baimicro.central;


import com.baimicro.central.ribbon.annotation.EnableFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16 23:56
 * @Description: TODO
 * version 0.1
 */
@EnableFeignClients
@EnableFeignInterceptor
@EnableDiscoveryClient
@EnableRedisHttpSession
@SpringBootApplication
public class OAuthServerApp {
    public static void main(String[] args) {
        SpringApplication.run(OAuthServerApp.class, args);
    }
}
