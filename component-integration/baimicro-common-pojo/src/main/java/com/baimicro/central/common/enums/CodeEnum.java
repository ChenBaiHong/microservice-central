package com.baimicro.central.common.enums;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO
 * version 0.1
 */
public enum CodeEnum {
    SUCCESS(0),
    ERROR(1);

    private Integer code;

    CodeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
