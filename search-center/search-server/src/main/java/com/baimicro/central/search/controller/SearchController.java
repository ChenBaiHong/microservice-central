package com.baimicro.central.search.controller;


import com.alibaba.fastjson.JSONObject;
import com.baimicro.central.common.model.PageResult;
import com.baimicro.central.search.model.SearchDto;
import com.baimicro.central.search.service.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO 通用搜索
 * version 0.1
 */
@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

    private final ISearchService searchService;

    public SearchController(ISearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     */
    @PostMapping("/{indexName}")
    public PageResult strQuery(@PathVariable String indexName, @RequestBody(required = false) SearchDto searchDto) throws IOException {
        if (searchDto == null) {
            searchDto = new SearchDto();
        }
        return searchService.strQuery(indexName, searchDto);
    }
}
