package com.baimicro.central.log.properties;

import com.zaxxer.hikari.HikariConfig;
import lombok.Setter;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: hospital-cloud-platform
 * @description:
 * 日志数据源配置
 * logType=db时生效(非必须)，如果不配置则使用当前数据源
 * @author: baiHoo.chen
 * @create: 2020-04-07
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "his.cloud.audit-log.datasource")
public class LogDbProperties extends HikariConfig {
}

