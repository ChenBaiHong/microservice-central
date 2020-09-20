package com.baimicro.central.user.service.impl;

import com.baimicro.central.common.constant.CommonConstant;
import com.baimicro.central.common.model.AppPerm;
import com.baimicro.central.user.IAppPermService;
import com.baimicro.central.user.mapper.PlatfPermissionMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-03-25
 * @Version: V1.0
 */
@Component
@Service(interfaceClass = IAppPermService.class)
public class PlatfPermissionServiceImpl implements IAppPermService{

    @Resource
    private PlatfPermissionMapper baseMapper;


    @Override
    public List<AppPerm> findPermissionByRoleIds(Set<Serializable> roleIds, Integer type) {
        if (CommonConstant.PERMISSION.equals(type)) {
            return baseMapper.findSimplePermissionByRoleIds(roleIds);
        } else {
            return baseMapper.findAllPermissionByRoleIds(roleIds);
        }
    }

    @Override
    public List<AppPerm> findPermissionByRoles(HashSet<Serializable> roleCodes, Integer type) {
        if (CommonConstant.PERMISSION.equals(type)) {
            return baseMapper.findSimplePermissionByRoleCodes(roleCodes);
        } else {
            return baseMapper.findAllPermissionByRoleCodes(roleCodes);
        }
    }
}
