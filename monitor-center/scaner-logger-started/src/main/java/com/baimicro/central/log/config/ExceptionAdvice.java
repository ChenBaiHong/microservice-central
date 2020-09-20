package com.baimicro.central.log.config;

import com.baimicro.central.common.exception.DefaultExceptionAdvice;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @program: hospital-cloud-platform
 * @description: 异常 AOP 切面通知
 * @author: baiHoo.chen
 * @create: 2020-05-12
 **/
@ControllerAdvice
public class ExceptionAdvice extends DefaultExceptionAdvice {
}
