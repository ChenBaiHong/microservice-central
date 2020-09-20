package com.baimicro.central.websocket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: hospital-oud-platform
 * @description: 声明式方接收到字符串消息时，对该方法进行回掉 注入参数类型：Session，String
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnMessage {
}
