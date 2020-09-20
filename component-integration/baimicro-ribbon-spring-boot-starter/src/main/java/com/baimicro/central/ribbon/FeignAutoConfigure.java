package com.baimicro.central.ribbon;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/15 17:28
 * @Description: TODO Feign统一配置
 * version 0.1
 */
public class  FeignAutoConfigure {

    /**
     * Feign 日志级别
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
