package com.baimicro.central.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 租户 holder
 * version 0.1
 */
public class TenantContextHolder {

    /**
     * 支持父子线程之间的数据传递
     */
    private static final ThreadLocal<String> CONTEXT = new TransmittableThreadLocal<>();

    public static void setTenant(String tenant) {
        CONTEXT.set(tenant);
    }

    public static String getTenant() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
