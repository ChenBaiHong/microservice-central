package com.baimicro.central.ribbon;

import com.baimicro.central.ribbon.config.RestTemplateProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.DefaultPropertiesFactory;
import org.springframework.context.annotation.Bean;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/15
 * @Description: TODO Ribbon扩展配置类
 * version 0.1
 */
@EnableConfigurationProperties(RestTemplateProperties.class)
public class RibbonAutoConfigure {
    @Bean
    public DefaultPropertiesFactory defaultPropertiesFactory() {
        return new DefaultPropertiesFactory();
    }
}

