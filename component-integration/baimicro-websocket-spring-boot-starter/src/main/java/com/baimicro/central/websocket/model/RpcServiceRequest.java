package com.baimicro.central.websocket.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: hospital-cloud-platform
 * @description: Rpc 服务调用请求
 * @author: baiHoo.chen
 * @create: 2020-04-21
 **/
@Getter
@Setter
public class RpcServiceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String requestId; // 请求对象的ID
    private String serviceName;// 类名
    private String methodName;// 函数名称
    private Class<?>[] parameterTypes;// 参数类型
    private Object[] parameters;// 参数列表

    public RpcServiceRequest() {
    }

    public RpcServiceRequest(String serviceName, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }
}
