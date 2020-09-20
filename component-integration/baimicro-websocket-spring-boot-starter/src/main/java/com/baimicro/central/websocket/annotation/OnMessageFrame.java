package com.baimicro.central.websocket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @project: hualida-scada
 * @author: chen.baihoo
 * @date: 2020/9/2
 * @Description: TODO
 * version 0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnMessageFrame {
}
