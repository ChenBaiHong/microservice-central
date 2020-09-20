package com.baimicro.central.websocket.annotation;

import com.baimicro.central.websocket.standard.ServerEndpointExporter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * @program: hospital-cloud-platform
 * @description:
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@ConditionalOnClass(ServerEndpointExporter.class)
@Configuration
public class NettyWebSocketSelector {

}
