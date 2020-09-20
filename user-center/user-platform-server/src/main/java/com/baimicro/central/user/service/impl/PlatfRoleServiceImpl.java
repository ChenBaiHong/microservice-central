package com.baimicro.central.user.service.impl;

import com.baimicro.central.common.model.AppRole;
import com.baimicro.central.user.IAppAuthService;
import com.baimicro.central.user.mapper.PlatfRoleMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-03-25
 * @Version: V1.0
 */
@Component
@Service(interfaceClass = IAppAuthService.class)
public class PlatfRoleServiceImpl implements IAppAuthService {

    @Resource
    private PlatfRoleMapper baseMapper;

    /**
     * 根据用户id获取角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<AppRole> findRolesByUserId(Serializable userId) {

        return baseMapper.findRolesByUserId(userId);
    }
}
