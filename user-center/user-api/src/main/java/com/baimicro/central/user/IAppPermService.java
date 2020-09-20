package com.baimicro.central.user;

import com.baimicro.central.common.model.AppPerm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: microservices-central
 * @description: 权限服务类
 * @author: baiHoo.chen
 * @create: 2020-06-22
 **/
public interface IAppPermService {

    /**
     * 根据用户 角色ID 获取详细权限
     *
     * @param roleIds
     * @param type
     * @return
     */
    List<AppPerm> findPermissionByRoleIds(Set<Serializable> roleIds, Integer type);
    /**
     * 根据用户 角色编码 获取详细权限
     *
     * @param roleCodes
     * @param type
     * @return
     */
    List<AppPerm> findPermissionByRoles(HashSet<Serializable> roleCodes, Integer type);
}
