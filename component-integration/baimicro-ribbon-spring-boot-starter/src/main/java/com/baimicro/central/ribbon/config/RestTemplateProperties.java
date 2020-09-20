package com.baimicro.central.ribbon.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/15
 * @Description: TODO RestTemplate 配置
 * version 0.1
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "his.cloud.rest-template")
public class RestTemplateProperties {
    /**
     * 最大链接数
     */
    private int maxTotal = 200;
    /**
     * 同路由最大并发数
     */
    private int maxPerRoute = 50;
    /**
     * 读取超时时间 ms
     */
    private int readTimeout = 35000;
    /**
     * 链接超时时间 ms
     */
    private int connectTimeout = 10000;
}
