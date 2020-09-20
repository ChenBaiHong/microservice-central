package com.baimicro.central.oauth2.common.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * @className PermitProperties
 * @Description TODO 配置需要放行的 url 白名单
 * @Author baigle.chen
 * @Date 2019-11-10 18:06
 * @Version 1.0
 */
public class PermitProperties {
    /**
     * 监控中心和swagger需要访问的url
     */
    private static final String[] ENDPOINTS = {
            "/oauth/**",
            "/actuator/**",
            "/*/v2/api-docs",
            "/swagger/api-docs",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/druid/**"
    };

    /**
     * 设置不用认证的url
     */
    private String[] httpUrls = {};

    public void setHttpUrls(String[] httpUrls) {
        this.httpUrls = httpUrls;
    }

    public String[] getUrls() {
        if (httpUrls == null || httpUrls.length == 0) {
            return ENDPOINTS;
        }
        List<String> list = new ArrayList<>();
        for (String url : ENDPOINTS) {
            list.add(url);
        }
        for (String url : httpUrls) {
            list.add(url);
        }
        return list.toArray(new String[list.size()]);
    }
}
