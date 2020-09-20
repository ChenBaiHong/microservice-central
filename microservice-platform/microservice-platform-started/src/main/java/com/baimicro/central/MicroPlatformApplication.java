package com.baimicro.central;

import com.baimicro.central.ribbon.annotation.EnableFeignInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @program: hospital-cloud-platform
 * @description: 微服务管理平台启动主类
 * @author: baiHoo.chen
 * @create: 2020-04-09
 **/
@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignInterceptor
public class MicroPlatformApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) throws UnknownHostException {

        ConfigurableApplicationContext application = SpringApplication.run(MicroPlatformApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        path = path == null ? "" : path;
        log.info("\n----------------------------------------------------------\n\t" +
                "Application  is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "----------------------------------------------------------");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MicroPlatformApplication.class);
    }
}
