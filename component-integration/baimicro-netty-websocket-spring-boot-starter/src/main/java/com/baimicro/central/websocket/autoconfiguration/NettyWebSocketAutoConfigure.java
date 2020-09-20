package com.baimicro.central.websocket.autoconfiguration;

import com.baimicro.central.websocket.annotation.EnableWebSocket;
import com.baimicro.central.websocket.standard.ServerEndpointConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @program: hospital-cloud-platform
 * @description: 自动配置类
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@EnableWebSocket
@EnableConfigurationProperties({ServerEndpointConfig.class})
public class NettyWebSocketAutoConfigure {

}
