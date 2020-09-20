package com.baimicro.central.common.exception;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 分布式锁异常
 * version 0.1
 */
public class LockException extends RuntimeException {


    public LockException(String message) {
        super(message);
    }
}
