package com.baimicro.central.search.client.service;

import com.baimicro.central.common.model.Result;
import com.baimicro.central.search.model.LogicDelDto;
import com.baimicro.central.search.model.SearchDto;

import java.util.Map;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO 搜索客户端接口
 * version 0.1
 */
public interface IQueryService {
    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     */
    Result strQuery(String indexName, SearchDto searchDto);

    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     * @param logicDelDto 逻辑删除Dto
     */
    Result strQuery(String indexName, SearchDto searchDto, LogicDelDto logicDelDto);

    /**
     * 访问统计聚合查询
     * @param indexName 索引名
     * @param routing es的路由
     */
    Map<String, Object> requestStatAgg(String indexName, String routing);
}

