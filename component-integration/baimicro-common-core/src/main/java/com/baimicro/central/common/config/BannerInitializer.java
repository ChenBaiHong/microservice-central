package com.baimicro.central.common.config;

import com.baimicro.central.common.constant.CommonConstant;
import com.baimicro.central.common.utils.CustomBanner;
import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.taobao.text.Color;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO Banner初始化
 * version 0.1
 */
public class BannerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!(applicationContext instanceof AnnotationConfigApplicationContext)) {
            LogoBanner logoBanner = new LogoBanner(BannerInitializer.class, "/his/logo.txt", "Welcome to his cloud platform", 5, 6, new Color[5], true);
            CustomBanner.show(logoBanner, new Description(BannerConstant.VERSION + ":", CommonConstant.PROJECT_VERSION, 0, 1)
                    , new Description("官网:", "http://gitlab.chenbaihoo.com", 0, 1)
                    , new Description("关于:", "http://www.chenbaihoo.com", 0, 1)
            );
        }
    }
}
