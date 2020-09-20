package com.baimicro.central.common.annotation;

import java.lang.annotation.*;

/**
 * 请求的方法参数SysUser上添加该注解，则注入当前登录人信息
 * 例1：public void test(@LoginUser AppUser user) //只有username 和 roles
 * 例2：public void test(@LoginUser(isFull = true) AppUser user) //能获取 AppUser 对象的所有信息
 *
 * @Description: TODO
 * @author: chen.baihoo
 * @date: 2020/2/14
 * version 0.1
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
    /**
     * 是否查询User对象所有信息，true 则通过rpc接口查询
     */
    boolean isFull() default false;
}
