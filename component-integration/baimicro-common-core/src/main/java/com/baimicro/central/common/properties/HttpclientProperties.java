package com.baimicro.central.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @program: hospital-cloud-platform
 * @description: httpClient 配置项
 * @author: baiHoo.chen
 * @create: 2020-04-21
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "his.cloud.httpclient")
@RefreshScope
public class HttpclientProperties {

    // 最大连接数
    private Integer maxTotal = 100;
    // 并发数
    private Integer defaultMaxPerRoute = 50;
    // 创建连接的最长时间
    private Integer connectTimeout = 100000;
    // 从连接池中获取到连接的最长时间
    private Integer connectionRequestTimeout = 5000;
    // 数据传输的最长时间
    private Integer socketTimeout = 100000;
    // 在不活动后验证
    private Integer validateAfterInactivity = 100000;
}
