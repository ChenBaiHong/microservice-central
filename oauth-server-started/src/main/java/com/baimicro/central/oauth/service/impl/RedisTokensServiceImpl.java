package com.baimicro.central.oauth.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.PageUtil;
import com.baimicro.central.common.constant.SecurityConstants;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.oauth.model.TokenVo;
import com.baimicro.central.oauth.service.ITokensService;
import com.baimicro.central.redis.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO token管理服务(redis token)
 * version 0.1
 */
@Slf4j
@Service
public class RedisTokensServiceImpl implements ITokensService {

    @Autowired
    private RedisUtil redisUtil;
    @Override
    public Result  listTokens(TokenVo tokenVo, Integer pageNo, Integer pageSize) {

        int[] startEnds = PageUtil.transToStartEnd(pageNo, pageSize);
        //根据请求参数生成redis的key
        String redisKey = getRedisKey(tokenVo);
        long size = redisUtil.lGetListSize(redisKey);
        List<TokenVo> result = new ArrayList<>(pageSize);
        //查询token集合
        List<Object> tokenObjs = redisUtil.lGet(redisKey, startEnds[0], startEnds[1]-1);
        if (tokenObjs != null) {
            for (Object obj : tokenObjs) {
                DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken)obj;
                //构造token对象
                tokenVo = new TokenVo();
                tokenVo.setTokenValue(accessToken.getValue());
                tokenVo.setExpiration(accessToken.getExpiration());

                //获取用户信息
                Object authObj = redisUtil.get(SecurityConstants.REDIS_TOKEN_AUTH + accessToken.getValue());
                OAuth2Authentication authentication = (OAuth2Authentication)authObj;
                if (authentication != null) {
                    OAuth2Request request = authentication.getOAuth2Request();
                    tokenVo.setUsername(authentication.getName());
                    tokenVo.setClientId(request.getClientId());
                    tokenVo.setGrantType(request.getGrantType());
                }

                result.add(tokenVo);
            }
        }
        IPage<TokenVo> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNo);
        page.setTotal(size);
        page.setRecords(result);
        return Result.succeed(page);
    }

    /**
     * 根据请求参数生成redis的key
     */
    private String getRedisKey(TokenVo tokenVo) {
        String result;
        if (StringUtils.isNoneEmpty(tokenVo.getClientId(),tokenVo.getUsername())) {
            result = SecurityConstants.REDIS_UNAME_TO_ACCESS + tokenVo.getClientId() + ":" + tokenVo.getUsername();
        } else{
            result = SecurityConstants.REDIS_CLIENT_ID_TO_ACCESS + tokenVo.getClientId();
        }
        return result;
    }
}

