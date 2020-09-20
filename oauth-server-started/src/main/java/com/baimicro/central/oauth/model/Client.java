package com.baimicro.central.oauth.model;

import com.baimicro.central.common.model.SuperEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO
 * version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("oauth_client_details")
public class Client extends SuperEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String clientId;
    /**
     * 应用名称
     */
    private String clientName;
    private String resourceIds = "";
    private String clientSecret;
    private String clientSecretStr;
    private String scope = "all";
    private String authorizedGrantTypes = "authorization_code,password,refresh_token,client_credentials";
    private String webServerRedirectUri;
    private String authorities = "";
    @TableField(value = "access_token_validity")
    private Integer accessTokenValiditySeconds = 18000;
    @TableField(value = "refresh_token_validity")
    private Integer refreshTokenValiditySeconds = 28800;
    private String additionalInformation = "{}";
    private String autoapprove = "true";

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String description;
}
