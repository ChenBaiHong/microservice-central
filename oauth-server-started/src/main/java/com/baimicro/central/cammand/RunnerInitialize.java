package com.baimicro.central.cammand;

import com.baimicro.central.common.constant.CacheConstant;
import com.baimicro.central.oauth.mapper.OauthDubboMapper;
import com.baimicro.central.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName RunnerInitialize
 * @Description TODO 执行一些初始化操作
 * @Author baiHoo.chen
 * @Date 2019/10/23 9:48
 */
@Slf4j
@Component
public class RunnerInitialize implements CommandLineRunner {

    @Resource
    private OauthDubboMapper oauthDubboMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void run(String... args) throws Exception {
        cacheClientDictitem();
    }

    /**
     * 初始化缓存字典项
     */
    public void cacheClientDictitem(){
        redisUtil.set(CacheConstant.CACHE_REGISTRY_DUBBO_SERVER,oauthDubboMapper.findRegistryDubboServer(),-1);
    }
}
