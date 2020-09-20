package com.baimicro.central.search.service;

import com.alibaba.fastjson.JSONObject;
import com.baimicro.central.common.model.PageResult;
import com.baimicro.central.search.model.SearchDto;

import java.io.IOException;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO
 * version 0.1
 */
public interface ISearchService {
    /**
     * StringQuery通用搜索
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     * @return
     */
    PageResult<JSONObject> strQuery(String indexName, SearchDto searchDto) throws IOException;
}

