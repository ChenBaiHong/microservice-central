package com.baimicro.central.ribbon.config;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.baimicro.central.ribbon.rule.VersionIsolationRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/15
 * @Description: TODO
 * version 0.1
 */
public class RuleConfigure {
    @Bean
    @ConditionalOnClass(NacosServer.class)
    @ConditionalOnMissingBean
    public IRule isolationRule() {
        return new VersionIsolationRule();
    }
}
