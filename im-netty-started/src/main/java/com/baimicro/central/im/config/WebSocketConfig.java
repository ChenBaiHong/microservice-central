package com.baimicro.central.im.config;

import com.baimicro.central.websocket.standard.ServerEndpointExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: hospital-cloud-platform
 * @description: 配置 websocket
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
