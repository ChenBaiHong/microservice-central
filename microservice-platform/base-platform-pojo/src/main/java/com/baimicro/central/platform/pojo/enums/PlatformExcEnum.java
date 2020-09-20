package com.baimicro.central.platform.pojo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @className BizEnum
 * @Description TODO 在线咨询异常枚举
 * @Author baigle.chen
 * @Date 2019-10-18 22:37
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum PlatformExcEnum {

    BIZ_ERR_CODE(10000, "业务未处理成功"),

    UNKNOWN_ERR_CODE(10001, "未知错误"),

    DB_ERR_CODE(10002, "数据库操作异常"),

    PROGRAM_ERR_CODE(10003, "程序运行异常"),

    DUPLICATE_KEY_ERR_CODE(1000005, "数据库中已存在该记录"),

    MISSING_PARAMETER(100010, "缺少参数"),

    NOT_ENTERPRISE_MEMBER(60111, "小程序未能检测到您属于该医院医生"),

    MISSING_CONSULT_ERROR(500, "因咨询订单详情不完整，无法开始咨询"),

    NOT_DISCERN_MEMBER(500, "小程序未被企业微信内部应用关联"),

    NOT_SAVE_ENTERPRISE_MEMBER(500, "未能检测到您开通在线咨询的身份信息")
    ;

    private Integer code;
    private String message;
}
