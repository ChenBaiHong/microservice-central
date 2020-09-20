package com.baimicro.central;

import com.baimicro.central.search.annotation.EnableSearchClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: hospital-cloud-platform
 * @description: 日志中心监听启动类
 * @author: baiHoo.chen
 * @create: 2020-05-12
 **/
@EnableDiscoveryClient
@EnableSearchClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LogApp {
    public static void main(String[] args) {
        SpringApplication.run(LogApp.class, args);
    }
}

