package com.baimicro.central.sentinel.config;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.baimicro.central.common.model.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO Sentinel配置类
 * version 0.1
 */
public class SentinelAutoConfigure {
    /**
     * 限流、熔断统一处理类
     */
    @Configuration
    @ConditionalOnClass(HttpServletRequest.class)
    public static class WebmvcHandler {
        @Bean
        public BlockExceptionHandler webmvcBlockExceptionHandler() {
            return (request, response, e) -> {
                response.setStatus(429);
                Result result = Result.failed(e.getMessage());
                response.getWriter().print(JSONUtil.toJsonStr(result));
            };
        }
    }

    /**
     * 限流、熔断统一处理类
     */
    @Configuration
    @ConditionalOnClass(ServerResponse.class)
    public static class WebfluxHandler {
        @Bean
        public BlockRequestHandler webfluxBlockExceptionHandler() {

            return (exchange, t) ->
                    ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(Result.failed(t.getMessage())));
        }
    }
}
