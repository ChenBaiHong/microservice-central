package com.baimicro.central.websocket.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @program: hospital-cloud-platform
 * @description: Rpc 服务调用
 * @author: baiHoo.chen
 * @create: 2020-04-20
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcService {

    @AliasFor(annotation = Component.class)
    String value() default "";
}
