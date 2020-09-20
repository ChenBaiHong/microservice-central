package com.baimicro.central.user;

import com.baimicro.central.common.model.AppUser;
import com.baimicro.central.common.model.LoginAppUser;

/**
 * @program: microservices-central
 * @description: 用户接口服务类
 * @author: baiHoo.chen
 * @create: 2020-06-22
 **/
public interface IAppUserService {

    /**
     * 通过 openId 和 客户端ID 获取登陆用户（包含用户详细的权限角色信息）
     *
     * @param openId
     * @param clientId
     * @return
     */
    default LoginAppUser findByOpenId(String openId, String clientId) {
        return new LoginAppUser();
    }

    /**
     * 通过 appUser 转换为 LoginAppUser，把 roles 和 permissions 也查询出来
     * （包含用户详细的权限角色信息）
     *
     * @param appUser
     * @return
     */
    default LoginAppUser getLoginAppUser(AppUser appUser) {
        return new LoginAppUser();
    }

    /**
     * 根据用户名 获取登陆对象（包含用户详细的权限角色信息）
     *
     * @param username
     * @return
     */
    default LoginAppUser findByUsername(String username) {
        return new LoginAppUser();
    }

    /**
     * 根据 OpenId 获取登陆对象（包含用户详细的权限角色信息）
     *
     * @param openId
     * @return
     */
    default LoginAppUser findByOpenId(String openId) {
        return new LoginAppUser();
    }

    /**
     * 根据手机号 获取登陆对象（包含用户详细的权限角色信息）
     *
     * @param mobile
     * @return
     */
    default LoginAppUser findByMobile(String mobile) {
        return new LoginAppUser();
    }

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    default AppUser selectByUsername(String username) {
        return new AppUser();
    }

    /**
     * 根据手机号查询用户
     *
     * @param mobile
     * @return
     */
    default AppUser selectByMobile(String mobile) {
        return new AppUser();
    }

    /**
     * 根据openId查询用户
     *
     * @param openId
     * @return
     */
    default AppUser selectByOpenId(String openId) {
        return new AppUser();
    }
}
