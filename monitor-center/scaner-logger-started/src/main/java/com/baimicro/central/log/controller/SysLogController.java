package com.baimicro.central.log.controller;

import com.baimicro.central.common.model.Result;
import com.baimicro.central.search.client.service.IQueryService;
import com.baimicro.central.search.model.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: hospital-cloud-platform
 * @description: 系统日志检索 前端控制器
 * @author: baiHoo.chen
 * @create: 2020-05-12
 **/
@RestController
public class SysLogController {
    @Autowired
    private IQueryService queryService;

    /**
     * 系统日志全文搜索列表
     * @param searchDto
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/sysLog")
    public Result getPage(SearchDto searchDto,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        searchDto.setIsHighlighter(true);
        searchDto.setSortCol("timestamp");
        searchDto.setLimit(pageSize);
        searchDto.setPage(pageNo);
        return queryService.strQuery("sys-log-*",  searchDto);
    }
}
