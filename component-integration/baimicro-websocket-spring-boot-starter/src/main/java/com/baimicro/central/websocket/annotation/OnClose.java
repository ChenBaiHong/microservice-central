package com.baimicro.central.websocket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @programhospital-cloud-platform
 * @descrion: 声明式方法注解，当有 websocket 连接关闭时，对方进行回掉注入参数的类型：session
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnClose {
}
