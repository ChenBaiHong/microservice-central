package com.baimicro.central.controller;

import com.baimicro.central.common.model.AppUser;
import com.baimicro.central.common.model.LoginAppUser;
import com.baimicro.central.rpcservice.DubboReference;
import com.baimicro.central.user.IAppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName SysUserController
 * @Description TODO
 * @Author baiHoo.chen
 * @Date 2020/3/16
 */
@Slf4j
@RestController
public class AppUserController {


    @Resource
    private DubboReference dubboReference;

    /**
     * 查询用户实体对象
     */
    @GetMapping(value = "/users/name/{username}", params = "clientId")
    public AppUser selectByUsername(@PathVariable String username, String clientId) {
        IAppUserService userService = dubboReference.getAppUserService(clientId);
        AppUser appUser = userService.selectByUsername(username);
        if (appUser != null)
            appUser.setClientId(clientId);
        return appUser;
    }

    /**
     * 查询用户登录对象LoginAppUser
     */
    @GetMapping(value = "/users-anon/login", params = {"username"})
    public LoginAppUser findByUsername(String username, HttpServletRequest request) {
        String clientId = request.getHeader("x-tenant-header");
        IAppUserService userService = dubboReference.getAppUserService(clientId);
        LoginAppUser appUser = userService.findByUsername(username);
        if (appUser != null)
            appUser.setClientId(clientId);
        return appUser;
    }

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    @GetMapping(value = "/users-anon/mobile", params = "mobile")
    public LoginAppUser findByMobile(String mobile,HttpServletRequest request) {
        String clientId = request.getHeader("x-tenant-header");
        IAppUserService userService = dubboReference.getAppUserService(clientId);
        LoginAppUser appUser = userService.findByMobile(mobile);
        if (appUser != null)
            appUser.setClientId(clientId);
        return appUser;
    }

    /**
     * 根据OpenId查询用户信息
     *
     * @param openId openId
     */
    @GetMapping(value = "/users-anon/openId", params = "openId")
    public LoginAppUser findByOpenId(String openId,HttpServletRequest request) {
        String clientId = request.getHeader("x-tenant-header");
        IAppUserService userService = dubboReference.getAppUserService(clientId);
        LoginAppUser appUser = userService.findByOpenId(openId);
        if (appUser != null)
            appUser.setClientId(clientId);
        return appUser;
    }
}

