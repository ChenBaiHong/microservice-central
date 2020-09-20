package com.baimicro.central.common.feign.fallback;

import com.baimicro.central.common.feign.UserService;
import com.baimicro.central.common.model.AppUser;
import com.baimicro.central.common.model.LoginAppUser;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/18
 * @Description: TODO userService降级工场
 * version 0.1
 */
@Slf4j
public class UserServiceFallbackFactory implements FallbackFactory<UserService> {
    @Override
    public UserService create(Throwable throwable) {
        return new UserService() {
            @Override
            public AppUser selectByUsername(String username) {
                log.error("通过用户名查询用户异常:{}", username, throwable);
                return new AppUser();
            }

            @Override
            public LoginAppUser findByUsername(String username) {
                log.error("通过用户名查询用户异常:{}", username, throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByMobile(String mobile) {
                log.error("通过手机号查询用户异常:{}", mobile, throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByOpenId(String openId) {
                log.error("通过openId查询用户异常:{}", openId, throwable);
                return new LoginAppUser();
            }
        };
    }
}
