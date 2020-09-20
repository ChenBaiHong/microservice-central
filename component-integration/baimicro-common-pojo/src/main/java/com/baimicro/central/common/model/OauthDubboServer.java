package com.baimicro.central.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
public class OauthDubboServer implements Serializable{

    /** 唯一主键 */
    private Integer id ;
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
}
