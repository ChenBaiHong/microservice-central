package com.baimicro.central.websocket.pojo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: hospital-cloud-platform
 * @description: 存储 Rpc 服务类实例
 * @author: baiHoo.chen
 * @create: 2020-04-21
 **/
public class StoreRpcServiceInstance {

    private static ConcurrentHashMap<String, Object> SERVICE_INSTANCE_MAP = new ConcurrentHashMap<>();


    /**
     * @param serviceName
     * @param serviceInstance
     */
    public static void put(String serviceName, Object serviceInstance) {
        SERVICE_INSTANCE_MAP.put(serviceName, serviceInstance);
    }

    /**
     * @param serviceName
     * @return
     */
    public static Object get(String serviceName) {
        return SERVICE_INSTANCE_MAP.get(serviceName);
    }

    public static ConcurrentHashMap<String, Object> getServiceInstanceMap() {
        return SERVICE_INSTANCE_MAP;
    }

    public static void setServiceInstanceMap(ConcurrentHashMap<String, Object> serviceInstanceMap) {
        SERVICE_INSTANCE_MAP = serviceInstanceMap;
    }
}
