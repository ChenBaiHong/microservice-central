package com.baimicro.central.gateway.feign.fallback;


import cn.hutool.core.collection.CollectionUtil;
import com.baimicro.central.gateway.feign.PermService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO PermService降级工场
 * version 0.1
 */
@Slf4j
@Component
public class PermServiceFallbackFactory implements FallbackFactory<PermService> {
    @Override
    public PermService create(Throwable throwable) {
        return roleIds -> {
            log.error("调用findByRoleCodes异常：{}", roleIds, throwable);
            return CollectionUtil.newArrayList();
        };
    }
}
