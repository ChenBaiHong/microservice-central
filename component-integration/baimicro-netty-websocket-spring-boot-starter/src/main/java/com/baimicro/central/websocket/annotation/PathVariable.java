package com.baimicro.central.websocket.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: hospital-cloud-platform
 * @description: 声明式参数注解
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PathVariable {


    @AliasFor("name")
    String value() default "";

    /**
     * 该注解方法 是去绑定一个 参数路径
     * @return
     */
    @AliasFor("value")
    String name() default "";

}
