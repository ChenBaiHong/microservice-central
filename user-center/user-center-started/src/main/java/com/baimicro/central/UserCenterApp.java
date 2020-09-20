package com.baimicro.central;

import com.baimicro.central.ribbon.annotation.EnableFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName UserCenterApp
 * @Description TODO 后台用户中心
 * @Author baiHoo.chen
 * @Date 2020/3/16 8:48
 */
@EnableDiscoveryClient
@EnableFeignInterceptor
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class UserCenterApp {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApp.class, args);
    }
}
