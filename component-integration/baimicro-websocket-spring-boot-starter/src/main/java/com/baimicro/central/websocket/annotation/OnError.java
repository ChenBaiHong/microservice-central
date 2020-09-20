package com.baimicro.central.websocket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: hospital-cloud-platform
 * @description: 声明式方法注解，当有 websocket 抛出异常时，对该方法进行回调 注入参数类型：session，Throwable
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnError {
}
