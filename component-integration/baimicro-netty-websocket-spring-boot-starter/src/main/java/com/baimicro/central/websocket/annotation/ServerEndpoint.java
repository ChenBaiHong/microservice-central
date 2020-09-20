package com.baimicro.central.websocket.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: hospital-cloud-platform
 * @description:
 *  声明式类注解，当ServerEndpointExporter类通过Spring配置进行声明并被使用，它将会去扫描带有@ServerEndpoint注解的类
 *  被注解的类将被注册成为一个WebSocket端点
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServerEndpoint {

    @AliasFor("path")
    String value() default "/";

    @AliasFor("value")
    String path() default "/";

    String[] subprotocols() default {};
}
