package com.baimicro.central.oauth.service;

import com.baimicro.central.common.model.PageResult;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.common.service.ISuperService;
import com.baimicro.central.oauth.model.Client;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO
 * version 0.1
 */
public interface IClientService extends ISuperService<Client> {

    Result saveClient(Client clientDto) throws Exception;

    Result delClient(Serializable id);

    Result delClient(List<Serializable> ids);
}
