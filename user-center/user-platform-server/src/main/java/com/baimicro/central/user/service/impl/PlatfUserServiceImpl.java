package com.baimicro.central.user.service.impl;

import com.baimicro.central.common.constant.CommonConstant;
import com.baimicro.central.common.enums.ExcEnum;
import com.baimicro.central.common.model.*;
import com.baimicro.central.user.IAppAuthService;
import com.baimicro.central.user.IAppPermService;
import com.baimicro.central.user.IAppUserService;
import com.baimicro.central.user.mapper.PlatfUserMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-03-25
 * @Version: V1.0
 */
@Component
@Service(interfaceClass = IAppUserService.class)
public class PlatfUserServiceImpl implements IAppUserService {

    @Resource
    private IAppAuthService authService;

    @Resource
    private IAppPermService appPermService;

    @Resource
    PlatfUserMapper baseMapper;

    /**
     * 获取UserDetails对象
     *
     * @param username
     * @return
     */
    @Override
    public LoginAppUser findByUsername(String username) {
        return getLoginAppUser(selectByUsername(username));
    }

    @Override
    public LoginAppUser findByOpenId(String openId) {
        return getLoginAppUser(selectByOpenId(openId));
    }

    @Override
    public LoginAppUser findByMobile(String mobile) {
        return getLoginAppUser(selectByMobile(mobile));
    }

    /**
     * 通过 appUser 转换为 LoginAppUser，把 roles 和 permissions 也查询出来
     *
     * @param appUser
     * @return
     */
    @Override
    public LoginAppUser getLoginAppUser(AppUser appUser) {
        if (appUser != null) {
            LoginAppUser loginAppUser = new LoginAppUser();
            BeanUtils.copyProperties(appUser, loginAppUser);
            List<AppRole> appRoles = authService.findRolesByUserId(appUser.getId());
            // 设置角色权限
            loginAppUser.setRoles(appRoles);
            if (!CollectionUtils.isEmpty(appRoles)) {
                Set<Serializable> roleIds = appRoles.parallelStream().map(SuperEntity::getId).collect(Collectors.toSet());
                List<AppPerm> appPerms = appPermService.findPermissionByRoleIds(roleIds, CommonConstant.PERMISSION);
                if (!CollectionUtils.isEmpty(appRoles)) {
                    Set<String> permissions = appPerms.parallelStream().map(p -> p.getPerms())
                            .collect(Collectors.toSet());
                    // 设置权限集合
                    loginAppUser.setPermissions(permissions);
                }
            }
            return loginAppUser;
        }
        return null;
    }

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    @Override
    public AppUser selectByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    /**
     * 根据手机号查询用户
     *
     * @param mobile
     * @return
     */
    @Override
    public AppUser selectByMobile(String mobile) {
        return baseMapper.selectByMobile(mobile);
    }

    /**
     * 根据openId查询用户
     *
     * @param openId
     * @return
     */
    @Override
    public AppUser selectByOpenId(String openId) {
        throw new RuntimeException(ExcEnum.NOT_OPENID_USER.getMessage());
    }
}
