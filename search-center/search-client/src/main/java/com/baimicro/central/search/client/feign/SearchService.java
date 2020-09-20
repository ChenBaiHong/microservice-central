package com.baimicro.central.search.client.feign;


import com.alibaba.fastjson.JSONObject;
import com.baimicro.central.common.constant.ServiceNameConstants;
import com.baimicro.central.common.model.PageResult;
import com.baimicro.central.search.client.feign.fallback.SearchServiceFallbackFactory;
import com.baimicro.central.search.model.SearchDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO
 * version 0.1
 */
@FeignClient(name = ServiceNameConstants.SEARCH_SERVICE, fallbackFactory = SearchServiceFallbackFactory.class, decode404 = true)
public interface SearchService {
    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     */
    @PostMapping(value = "/search/{indexName}")
    PageResult strQuery(@PathVariable("indexName") String indexName, @RequestBody SearchDto searchDto);
}
