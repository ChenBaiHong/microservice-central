package com.baimicro.central.gateway.filter;

import cn.hutool.core.util.IdUtil;
import com.baimicro.central.common.constant.CommonConstant;
import com.baimicro.central.log.properties.TraceProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO 生成日志链路追踪id，并传入header中
 * version 0.1
 */
@Component
public class TraceFilter implements GlobalFilter, Ordered {
    @Autowired
    private TraceProperties traceProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (traceProperties.getEnable()) {
            //链路追踪id
            String traceId = IdUtil.fastSimpleUUID();
            MDC.put(CommonConstant.LOG_TRACE_ID, traceId);
            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                    .headers(h -> h.add(CommonConstant.TRACE_ID_HEADER, traceId))
                    .build();

            ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
            return chain.filter(build);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
