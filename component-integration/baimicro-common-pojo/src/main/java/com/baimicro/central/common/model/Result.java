package com.baimicro.central.common.model;

import com.baimicro.central.common.enums.CodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(value = "handler")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public Result(T result, Integer code, String message, boolean success) {
        this.result = result;
        this.code = code;
        this.message = message;
        this.success = success;
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

    public static <T> Result<T> succeed() {
        return succeedWith(null, CodeEnum.SUCCESS.getCode(), null);
    }

    public static <T> Result<T> succeed(String msg) {
        return succeedWith(null, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return succeedWith(model, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model) {
        return succeedWith(model, CodeEnum.SUCCESS.getCode(), "");
    }

    public static <T> Result<T> succeedWith(T result, Integer code, String msg) {
        return new Result<>(result, code, msg);
    }

    public static <T> Result<T> failed(String msg) {
        return failedWith(null, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return failedWith(model, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failedWith(T result, Integer code, String msg) {
        return new Result<>(result, code, msg, false);
    }


}
