package com.baimicro.central.log.config;

import org.slf4j.TtlMDCAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 初始化TtlMDCAdapter实例，并替换MDC中的adapter对象
 * version 0.1
 */
public class TtlMDCAdapterInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        //加载TtlMDCAdapter实例
        TtlMDCAdapter.getInstance();
    }
}
