package com.baimicro.central.log.annotation;

import java.lang.annotation.*;

/**
 * @program: hospital-cloud-platform
 * @description: 审计日志注解
 * @author: baiHoo.chen
 * @create: 2020-04-07
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {
    /**
     * 操作信息
     */
    String operation();
}
