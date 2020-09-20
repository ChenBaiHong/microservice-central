package com.baimicro.central.ribbon.config;

import cn.hutool.core.util.StrUtil;

import com.baimicro.central.common.constant.CommonConstant;
import com.baimicro.central.common.constant.SecurityConstants;
import com.baimicro.central.common.context.TenantContextHolder;
import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/15
 * @Description: TODO feign 拦截器，只包含基础数据
 * version 0.1
 */
public class FeignInterceptorConfig {
    /**
     * 使用feign client访问别的微服务时，将上游传过来的client、traceid等信息放入header传递给下一个服务
     */
    @Bean
    public RequestInterceptor baseFeignInterceptor() {
        return template -> {
            //传递client
            String tenant = TenantContextHolder.getTenant();
            if (StrUtil.isNotEmpty(tenant)) {
                template.header(SecurityConstants.TENANT_HEADER, tenant);
            }

            //传递日志traceId
            String traceId = MDC.get(CommonConstant.LOG_TRACE_ID);
            if (StrUtil.isNotEmpty(traceId)) {
                template.header(CommonConstant.TRACE_ID_HEADER, traceId);
            }
        };
    }
}

