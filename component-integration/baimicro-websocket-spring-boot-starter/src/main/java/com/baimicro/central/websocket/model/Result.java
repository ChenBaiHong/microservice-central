package com.baimicro.central.websocket.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO
 * version 0.1
 */
@Getter
@Setter
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 响应ID
     */
    private String requestId;

    /**
     * 成功标志
     */
    private boolean success = true;

    /**
     * 返回处理消息
     */
    private String message = "操作成功！";

    /**
     * 返回代码
     */
    private Integer code = 0;

    /**
     * 返回数据对象 data
     */
    private T result;

    public Result() {

    }

    public Result(T result) {
        this.result = result;
    }

    public Result(T result, Integer code, String message) {
        this.result = result;
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
