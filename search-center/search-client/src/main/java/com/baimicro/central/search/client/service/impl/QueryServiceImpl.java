package com.baimicro.central.search.client.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baimicro.central.common.model.PageResult;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.search.client.feign.AggregationService;
import com.baimicro.central.search.client.feign.SearchService;
import com.baimicro.central.search.client.service.IQueryService;
import com.baimicro.central.search.model.LogicDelDto;
import com.baimicro.central.search.model.SearchDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO 搜索客户端Service
 * version 0.1
 */
public class QueryServiceImpl implements IQueryService {

    @Resource
    private SearchService searchService;

    @Resource
    private AggregationService aggregationService;

    @Override
    public Result strQuery(String indexName, SearchDto searchDto) {
        return strQuery(indexName, searchDto, null);
    }

    @Override
    public Result strQuery(String indexName, SearchDto searchDto, LogicDelDto logicDelDto) {
        setLogicDelQueryStr(searchDto, logicDelDto);
        PageResult<JSONObject> pageResult = searchService.strQuery(indexName, searchDto);
        IPage<JSONObject> page = new Page<>();
        page.setCurrent(searchDto.getPage());
        page.setSize(searchDto.getLimit());
        page.setRecords(pageResult.getData());
        page.setTotal(pageResult.getCount());
        return Result.succeed(page);
    }

    /**
     * 拼装逻辑删除的条件
     *
     * @param searchDto   搜索dto
     * @param logicDelDto 逻辑删除dto
     */
    private void setLogicDelQueryStr(SearchDto searchDto, LogicDelDto logicDelDto) {
        if (logicDelDto != null
                && StrUtil.isNotEmpty(logicDelDto.getLogicDelField())
                && StrUtil.isNotEmpty(logicDelDto.getLogicNotDelValue())) {
            String result;
            //搜索条件
            String queryStr = searchDto.getQueryStr();
            //拼凑逻辑删除的条件
            String logicStr = logicDelDto.getLogicDelField() + ":" + logicDelDto.getLogicNotDelValue();
            if (StrUtil.isNotEmpty(queryStr)) {
                result = "(" + queryStr + ") AND " + logicStr;
            } else {
                result = logicStr;
            }
            searchDto.setQueryStr(result);
        }
    }

    /**
     * 访问统计聚合查询
     *
     * @param indexName 索引名
     * @param routing   es的路由
     */
    @Override
    public Map<String, Object> requestStatAgg(String indexName, String routing) {
        return aggregationService.requestStatAgg(indexName, routing);
    }
}

