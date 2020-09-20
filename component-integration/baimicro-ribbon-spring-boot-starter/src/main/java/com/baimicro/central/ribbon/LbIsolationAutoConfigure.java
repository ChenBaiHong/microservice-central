package com.baimicro.central.ribbon;


import com.baimicro.central.ribbon.config.RuleConfigure;
import com.baimicro.central.common.constant.ConfigConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/15
 * @Description: TODO Ribbon扩展配置类
 * version 0.1
 */
@ConditionalOnProperty(value = ConfigConstants.CONFIG_RIBBON_ISOLATION_ENABLED, havingValue = "true")
@RibbonClients(defaultConfiguration = {RuleConfigure.class})
public class LbIsolationAutoConfigure {
}
