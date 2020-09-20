package com.baimicro.central.oauth.model;

import com.baimicro.central.common.model.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/12
 * @Description: TODO
 * version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(value = "handler")
@TableName("oauth_dubbo_server")
public class OauthServer extends SuperEntity {

    /** 唯一主键 */
    private Long id ;
    /** 应用客户端标示 */
    private String clientId ;
    /** 服务接口类 */
    private String serviceInterface ;
    /** 服务描述 */
    private String serviceDescription ;
    /** 注册服务名称 */
    private String serverName ;
    /** 注册中心地址 */
    private String registryCenterUrl ;
    /** 乐观锁 */
    private Integer reversion ;
    /** 创建人 */
    private String createBy ;
    /** 创建时间 */
    private LocalDate createTime ;
    /** 更新人 */
    private String updateBy ;
    /** 更新时间 */
    private LocalDate updateTime ;
}
