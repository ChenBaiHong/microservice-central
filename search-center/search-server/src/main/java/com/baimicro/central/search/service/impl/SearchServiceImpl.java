package com.baimicro.central.search.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baimicro.central.common.model.PageResult;
import com.baimicro.central.elasticsearch.utils.SearchBuilder;
import com.baimicro.central.search.model.SearchDto;
import com.baimicro.central.search.service.ISearchService;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO 通用搜索
 * version 0.1
 */
@Service
public class SearchServiceImpl implements ISearchService {
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    public SearchServiceImpl(ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    /**
     * StringQuery通用搜索
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     * @return
     */
    @Override
    public PageResult<JSONObject> strQuery(String indexName, SearchDto searchDto) throws IOException {
        return SearchBuilder.builder(elasticsearchRestTemplate, indexName)
                .setStringQuery(searchDto.getQueryStr())
                .addSort(searchDto.getSortCol(), SortOrder.DESC)
                .setIsHighlight(searchDto.getIsHighlighter())
                .getPage(searchDto.getPage(), searchDto.getLimit());
    }
}
