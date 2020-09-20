package com.baimicro.central.search.client.feign.fallback;


import com.alibaba.fastjson.JSONObject;
import com.baimicro.central.common.model.PageResult;
import com.baimicro.central.search.client.feign.SearchService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO searchService降级工场
 * version 0.1
 */
@Slf4j
public class SearchServiceFallbackFactory implements FallbackFactory<SearchService> {
    @Override
    public SearchService create(Throwable throwable) {
        return (indexName, searchDto) -> {
            log.error("通过索引{}搜索异常:{}", indexName, throwable);
            return PageResult.<JSONObject>builder().build();
        };
    }
}
