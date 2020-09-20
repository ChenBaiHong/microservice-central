package com.baimicro.central.swagger2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO
 * version 0.1
 */
@Configuration
@ConditionalOnProperty(name = "his.cloud.swagger.enabled", matchIfMissing = true)
@Import({
        Swagger2DocumentationConfiguration.class
})
public class Swagger2Configuration {
}
