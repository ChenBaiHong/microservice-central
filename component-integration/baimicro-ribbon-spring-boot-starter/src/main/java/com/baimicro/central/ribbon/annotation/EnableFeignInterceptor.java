package com.baimicro.central.ribbon.annotation;

import com.baimicro.central.ribbon.config.FeignHttpInterceptorConfig;
import com.baimicro.central.ribbon.config.FeignInterceptorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: TODO 开启feign拦截器传递数据给下游服务，包含基础数据和http的相关数据
 * @author: chen.baihoo
 * @date: 2020/2/15 17:33
 * version 0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({FeignInterceptorConfig.class, FeignHttpInterceptorConfig.class})
public @interface EnableFeignInterceptor {

}
