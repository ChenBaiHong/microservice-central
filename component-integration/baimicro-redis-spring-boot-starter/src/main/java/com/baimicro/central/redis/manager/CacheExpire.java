package com.baimicro.central.redis.manager;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @ClassName CacheExpire
 * @Description TODO
 * @Author baiHoo.chen
 * @Date 2019/11/21 21:34
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheExpire {

    @AliasFor("expire")
    int value() default 120;

    /***
     *
     * @Author baihoo.chen
     * @Description TODO 时间已毫秒为单位
     * @Date 2019/11/21 21:37
     * @Param []
     * @return int
     **/
    @AliasFor("value")
    int expire() default 120;
}
