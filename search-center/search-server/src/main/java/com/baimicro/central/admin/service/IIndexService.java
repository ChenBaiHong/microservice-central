package com.baimicro.central.admin.service;

import com.baimicro.central.admin.model.IndexDto;
import com.baimicro.central.common.model.PageResult;
import com.baimicro.central.common.model.Result;

import java.io.IOException;
import java.util.Map;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO 索引
 * version 0.1
 */
public interface IIndexService {
    /**
     * 创建索引
     */
    boolean create(IndexDto indexDto) throws IOException;

    /**
     * 删除索引
     * @param indexName 索引名
     */
    boolean delete(String indexName) throws IOException;

    /**
     * 索引列表
     * @param queryStr 搜索字符串
     * @param indices 默认显示的索引名
     */
    Result list(String queryStr, String indices) throws IOException;

    /**
     * 显示索引明细
     * @param indexName 索引名
     */
    Map<String, Object> show(String indexName) throws IOException;
}
