package com.baimicro.central.oauth.service.impl;

import com.baimicro.central.common.constant.CacheConstant;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.common.service.impl.SuperServiceImpl;
import com.baimicro.central.oauth.mapper.OauthDubboMapper;
import com.baimicro.central.oauth.model.OauthServer;
import com.baimicro.central.oauth.service.IOauthDubboService;
import com.baimicro.central.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/12
 * @Description: TODO
 * version 0.1
 */
@Slf4j
@Service
public class OauthDubboServiceImpl extends SuperServiceImpl<OauthDubboMapper, OauthServer> implements IOauthDubboService {


    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Result delDubboServer(Serializable id) {
        if(removeById(id)){
            return Result.succeed("删除成功！");
        }
        return Result.failed("删除失败错误");
    }

    @Override
    public Result delDubboServer(List<Serializable> ids) {
        if(removeByIds(ids)){
            return Result.succeed("删除成功！");
        }
        return Result.failed("删除失败错误");
    }

    @Override
    public Result saveOauthDubboServer(OauthServer oauthServer) {
        if(saveOrUpdate(oauthServer)){
            return Result.succeed("保存成功！");
        }
        return Result.failed("保存失败错误");
    }

    @Override
    public Result refreshCache() {
        redisUtil.set(CacheConstant.CACHE_REGISTRY_DUBBO_SERVER, baseMapper.findRegistryDubboServer(), -1);
        return null;
    }
}
