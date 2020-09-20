package com.baimicro.central.oauth.service;

import com.baimicro.central.common.model.Result;
import com.baimicro.central.common.service.ISuperService;
import com.baimicro.central.oauth.model.OauthServer;

import java.io.Serializable;
import java.util.List;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/12
 * @Description: TODO
 * version 0.1
 */
public interface IOauthDubboService extends ISuperService<OauthServer> {

    Result delDubboServer(Serializable id);

    Result delDubboServer(List<Serializable> ids);

    Result saveOauthDubboServer(OauthServer oauthServer);

    Result refreshCache();
}
