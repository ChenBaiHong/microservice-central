package com.baimicro.central.search.client.feign.fallback;

import cn.hutool.core.map.MapUtil;
import com.baimicro.central.search.client.feign.AggregationService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO searchService降级工场
 * version 0.1
 */
@Slf4j
@Component
public class AggregationServiceFallbackFactory implements FallbackFactory<AggregationService> {
    @Override
    public AggregationService create(Throwable throwable) {
        return (indexName, routing) -> {
            log.error("通过索引{}搜索异常:{}", indexName, throwable);
            return MapUtil.newHashMap(0);
        };
    }
}
