package com.baimicro.central.gateway.feign;

import com.baimicro.central.common.constant.ServiceNameConstants;
import com.baimicro.central.common.model.AppPerm;
import com.baimicro.central.gateway.feign.fallback.PermServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO
 * version 0.1
 */
@FeignClient(name = ServiceNameConstants.USER_SERVICE, fallbackFactory = PermServiceFallbackFactory.class, decode404 = true)
public interface PermService {
    /**
     * 角色菜单列表
     *
     * @param roleCodes
     */
    @GetMapping(value = "/perms/{roleCodes}")
    List<AppPerm> findByRoleCodes(@PathVariable("roleCodes") String roleCodes);
}
