package com.baimicro.central.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 负载均衡策略 Holder
 * version 0.1
 */
public class LbIsolationContextHolder {

    private static final ThreadLocal<String> VERSION_CONTEXT = new TransmittableThreadLocal<>();

    public static void setVersion(String version) {
        VERSION_CONTEXT.set(version);
    }

    public static String getVersion() {
        return VERSION_CONTEXT.get();
    }

    public static void clear() {
        VERSION_CONTEXT.remove();
    }
}
