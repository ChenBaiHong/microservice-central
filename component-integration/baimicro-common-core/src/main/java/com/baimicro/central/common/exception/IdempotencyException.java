package com.baimicro.central.common.exception;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 幂等性异常
 * version 0.1
 */
public class IdempotencyException extends RuntimeException {


    public IdempotencyException(String message) {
        super(message);
    }
}
