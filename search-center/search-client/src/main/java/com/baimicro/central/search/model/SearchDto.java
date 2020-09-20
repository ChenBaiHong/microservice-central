package com.baimicro.central.search.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO
 * version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SearchDto implements Serializable {

    /**
     * 搜索关键字
     */
    private String queryStr;
    /**
     * 当前页数
     */
    private Integer page;
    /**
     * 每页显示数
     */
    private Integer limit;
    /**
     * 排序字段
     */
    private String sortCol;
    /**
     * 是否显示高亮
     */
    private Boolean isHighlighter;
    /**
     * es的路由
     */
    private String routing;
}
