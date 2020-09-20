package com.baimicro.central.common.annotation;

import java.lang.annotation.*;

/**
 * 请求的方法参数上添加该注解，则注入当前登录账号的应用id
 * 例：public void test(@LoginClient String clientId) //注入webApp
 *
 * @Description: TODO
 * @author: chen.baihoo
 * @date: 2020/2/14
 * version 0.1
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginClient {
}
