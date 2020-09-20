package com.baimicro.central.platform.system.service;


import com.baimicro.central.platform.pojo.entity.PlatfUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface IPlatfUserRoleService extends IService<PlatfUserRole> {


    /**
     * 查询所有的用户角色信息
     *
     * @return
     */
    Map<Long, String> queryUserRole();
}
