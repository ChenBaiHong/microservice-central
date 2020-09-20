package com.baimicro.central.websocket.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: hospital-cloud-platform
 * @description: 声明式注解类，当 springboot 启动类配置该注解项，即可将该组件 baihealth-netty-websocket-spring-boot-starter
 * 交由 spring 容器管理
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(NettyWebSocketSelector.class)
public @interface EnableWebSocket {
}
