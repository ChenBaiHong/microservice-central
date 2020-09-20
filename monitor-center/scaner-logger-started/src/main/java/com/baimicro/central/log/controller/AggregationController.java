package com.baimicro.central.log.controller;

import com.baimicro.central.search.client.service.IQueryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: hospital-cloud-platform
 * @description: 聚合检索 前端控制器
 * @author: baiHoo.chen
 * @create: 2020-05-12
 **/
@RestController
public class AggregationController {
    @Autowired
    private IQueryService queryService;

    @ApiOperation(value = "访问统计")
    @GetMapping(value = "/requestStat")
    public Map<String, Object> requestStatAgg() {
        return queryService.requestStatAgg("point-log-*","request-statistics");
    }
}

