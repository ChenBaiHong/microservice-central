package com.baimicro.central.config;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/25
 * @Description: TODO 注册 /actuator 公开的API集
 * version 0.1
 */
@Configuration
public class ActuatorConfig {


    /**
     * @use 使用方式 GET 获取 /actuator/caches
     * @description 显示内存中 HTTP跟踪信息（默认情况下，最近100个HTTP请求-响应交换）。需要一个 HttpTraceRepository bean。
     * @return
     */
    @Bean
    public HttpTraceRepository getHttpTraceRepository(){
        return new InMemoryHttpTraceRepository();
    }
}
