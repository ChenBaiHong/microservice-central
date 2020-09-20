package com.baimicro.central.common.exception;

import com.baimicro.central.common.enums.ExcEnum;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 业务异常
 * version 0.1
 */
public class BusinessException extends RuntimeException {

    private Integer code;
    private String message;


    public BusinessException(ExcEnum excEnum) {
        super(excEnum.getCode() + "," + excEnum.getMessage());
        this.code = excEnum.getCode();
        this.message = excEnum.getMessage();
    }

    public BusinessException(ExcEnum excEnum, String message) {
        super(excEnum.getCode() + "," + message);
        this.code = excEnum.getCode();
        this.message = message;
    }

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
