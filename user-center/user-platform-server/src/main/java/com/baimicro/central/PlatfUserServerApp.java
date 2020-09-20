package com.baimicro.central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName UserCenterApp
 * @Description TODO 后台用户中心
 * @Author baiHoo.chen
 * @Date 2020/3/16 8:48
 */
@EnableDiscoveryClient
@SpringBootApplication
public class PlatfUserServerApp {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PlatfUserServerApp.class);
        application.run(args);
    }
}
