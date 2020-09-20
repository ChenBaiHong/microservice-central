package com.baimicro.central.websocket.exception;

/**
 * @program: hospital-cloud-platform
 * @description: 自定义处理异常
 * @author: baiHoo.chen
 * @create: 2020-04-12
 **/
public class DeploymentException extends RuntimeException {


    public DeploymentException(String message) {
        super(message);
    }

    public DeploymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
