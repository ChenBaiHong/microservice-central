package com.baimicro.central.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @className BizEnum
 * @Description TODO
 * @Author baigle.chen
 * @Date 2019-10-18 22:37
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ExcEnum {

    BIZ_ERR_CODE(10000, "业务未处理成功"),

    UNKNOWN_ERR_CODE(10001, "未知错误"),

    DB_ERR_CODE(10002, "数据库操作异常"),

    PROGRAM_ERR_CODE(10003, "程序运行异常"),

    CLIENT_DISTINGUISH_MISS(30000, "clientId 对应的信息不存在"),

    DUPLICATE_KEY_ERR_CODE(30001, "数据库中已存在该记录"),

    MISSING_PARAMETER(30002, "缺少参数"),

    USER_PWD_ERROR(30003, "用户名或密码错误"),

    MOBILE_PWD_ERROR(30004, "手机号或密码错误"),

    OPENID_ERROR(30005, "openId 错误"),

    CLIENT_SECRET_MISMATCH(30006, "clientSecret 不匹配"),

    USER_DISABLED(30007, "用户已作废"),

    NOT_OPENID_USER(30008, "您好！用户该应用不支持 openId 登录"),

    NOT_MOBILE_USER(30008, "您好！用户该应用不支持 手机号码 登录"),

    REFRESH_TOKEN_ERROR(30009, "刷新 Token 错误"),

    USER_SERVICE_NOT_EXIST(400001, "系统用户服务正在升级...! 请稍后再试"),
    AUTH_SERVICE_NOT_EXIST(400002, "系统角色服务正在升级...! 请稍后再试"),
    PERM_SERVICE_NOT_EXIST(400003, "系统权限服务正在升级...! 请稍后再试"),

    WS_TOKEN_ERROR(500, "通信握手时用户身份信息Token异常"),
    ;


    private Integer code;
    private String message;
}
