package com.baimicro.central.oauth.service.impl;


import com.baimicro.central.common.constant.CommonConstant;
import com.baimicro.central.common.constant.SecurityConstants;
import com.baimicro.central.common.lock.DistributedLock;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.common.service.impl.SuperServiceImpl;
import com.baimicro.central.oauth.mapper.ClientMapper;
import com.baimicro.central.oauth.model.Client;
import com.baimicro.central.oauth.service.IClientService;
import com.baimicro.central.redis.template.RedisRepository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO
 * version 0.1
 */
@Slf4j
@Service
public class ClientServiceImpl extends SuperServiceImpl<ClientMapper, Client> implements IClientService {

    private final static String LOCK_KEY_CLIENTID = CommonConstant.LOCK_KEY_PREFIX + "clientId:";

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DistributedLock lock;

    @Override
    public Result saveClient(Client client) throws Exception {
        client.setClientSecret(passwordEncoder.encode(client.getClientSecretStr()));
        String clientId = client.getClientId();
        super.saveOrUpdateIdempotency(client, lock
                , LOCK_KEY_CLIENTID+clientId
                , new QueryWrapper<Client>().eq("client_id", clientId)
                , clientId + "已存在");
        return Result.succeed("操作成功");
    }



    @Override
    public Result delClient(Serializable id) {
        String clientId = baseMapper.selectById(id).getClientId();
        baseMapper.deleteById(id);
        redisRepository.del(clientRedisKey(clientId));
        return Result.succeed();
    }

    @Override
    public Result delClient(List<Serializable> ids) {
        List<Client> list = baseMapper.selectBatchIds(ids);
        baseMapper.deleteBatchIds(ids);
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = clientRedisKey(list.get(i).getClientId());
        }
        redisRepository.del(array);
        return Result.succeed();
    }

    private String clientRedisKey(String clientId) {
        return SecurityConstants.CACHE_CLIENT_KEY + ":" + clientId;
    }
}
