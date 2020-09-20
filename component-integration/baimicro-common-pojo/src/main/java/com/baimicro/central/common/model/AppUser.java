package com.baimicro.central.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO
 * version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(value = "handler")
public class AppUser extends SuperEntity {

    // 用户 所属 客户端
    private String clientId;
    // 用户姓名
    private String username;
    // 用户 昵称
    private String nickname;
    // 用户 登录密码
    private String password;
    // 用户 真实姓名
    private String realName;
    // 用户 头像
    private String headImgUrl;
    // 用户 性别
    private String sex;
    // 用户 openId
    private String openId;
    // 用户 unionId
    private String unionId;
    // 用户 手机号码
    private String mobile;
    // 用户 类型
    private String type;

    // 用户 身份是否有效
    private Boolean enabled;

    // tokenKey 存储用户衍生信息
    private String tokenKey;

    // 用户 角色身份
    private List<AppRole> roles;

}
