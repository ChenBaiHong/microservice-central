package com.baimicro.central;

import com.baimicro.central.ribbon.annotation.EnableBaseFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO
 * version 0.1
 */
@EnableFeignClients
@EnableBaseFeignInterceptor
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GatewayApp {
    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(GatewayApp.class);
        // 设置告知 SpringBoot 启动为 WebFlux 响应式 WEB 工程
        application.setWebApplicationType(WebApplicationType.REACTIVE);
        application.run(args);
    }
}
