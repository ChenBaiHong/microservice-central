package com.baimicro.central.user;

import com.baimicro.central.common.model.AppRole;

import java.io.Serializable;
import java.util.List;

/**
 * @program: microservices-central
 * @description: 角色服务
 * @author: baiHoo.chen
 * @create: 2020-06-22
 **/
public interface IAppAuthService {

    /**
     * 根据用户id获取角色
     *
     * @param userId
     * @return
     */
    List<AppRole> findRolesByUserId(Serializable userId);
}
