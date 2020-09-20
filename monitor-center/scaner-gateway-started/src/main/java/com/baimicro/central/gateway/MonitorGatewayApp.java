package com.baimicro.central.gateway;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: hospital-cloud-platform
 * @description: 网关监听服务启动类
 * @author: baiHoo.chen
 * @create: 2020-05-12
 **/
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MonitorGatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(MonitorGatewayApp.class, args);
    }
}
