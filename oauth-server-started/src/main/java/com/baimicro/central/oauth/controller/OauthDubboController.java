package com.baimicro.central.oauth.controller;

import com.baimicro.central.common.model.OauthDubboServer;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.oauth.model.OauthServer;
import com.baimicro.central.oauth.service.IOauthDubboService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/12
 * @Description: TODO
 * version 0.1
 */
@RestController
@RequestMapping("/oauthDubbo")
public class OauthDubboController {

    @Autowired
    private IOauthDubboService oauthDubboService;

    @GetMapping(value = "/list")
    public Result<IPage<OauthServer>> list(OauthDubboServer dubboServer,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<OauthServer>> result = new Result<>();
        Page<OauthServer> page = new Page<>(pageNo, pageSize);

        QueryWrapper<OauthServer> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNoneEmpty(dubboServer.getClientId())){
            queryWrapper.like("client_id",dubboServer.getServerName());
        }
        if(StringUtils.isNoneEmpty(dubboServer.getServerName())){
            queryWrapper.like("server_name",dubboServer.getServerName());
        }
        queryWrapper.orderByAsc("client_id");
        IPage<OauthServer> pageList = oauthDubboService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    @GetMapping("/{id}")
    public Result<OauthServer> get(@PathVariable Long id) {
        return Result.succeed(oauthDubboService.getById(id));
    }

    @GetMapping("/all")
    public Result<List<OauthServer>> allClient() {
        return Result.succeed(oauthDubboService.list());
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */

    @DeleteMapping(value = "/delete")
    public Result delete(@RequestParam(name = "id") String id) {
        return oauthDubboService.delDubboServer(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result deleteBatch(@RequestParam(name = "ids") String ids) {
        return oauthDubboService.delDubboServer(Arrays.asList(ids.split(",")));
    }

    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody OauthServer oauthServer) {
        return oauthDubboService.saveOauthDubboServer(oauthServer);
    }

    /**
     * @功能：刷新缓存
     * @return
     */
    @GetMapping(value = "/refreshCache")
    public Result refreshCache() {

        return oauthDubboService.refreshCache();
    }
}
