package com.baimicro.central.search.annotation;


import com.baimicro.central.search.client.feign.fallback.SearchServiceFallbackFactory;
import com.baimicro.central.search.client.service.impl.QueryServiceImpl;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO 控制是否加载搜索中心客户端的Service
 * version 0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableFeignClients(basePackages = "com.baimicro.central")
@Import({SearchServiceFallbackFactory.class, QueryServiceImpl.class})
public @interface EnableSearchClient {

}
