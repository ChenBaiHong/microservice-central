package com.baimicro.central.oauth2.common.constants;


/**
 * @className AuthoritiesEnum
 * @Description TODO 权限常量
 * @Author baigle.chen
 * @Date 2019-11-10 15:22
 * @Version 1.0
 */

public enum AuthoritiesEnum {

    // 管理员角色
    ADMIN("ROLE_ADMIN"),

    // 普通用户角色
    USER("ROLE_USER"),

    //匿名用户角色
    ANONYMOUS("ROLE_ANONYMOUS");

    private String role;


    AuthoritiesEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
