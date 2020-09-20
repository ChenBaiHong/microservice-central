package com.baimicro.central.controller;

import com.baimicro.central.common.constant.CommonConstant;
import com.baimicro.central.common.model.AppPerm;
import com.baimicro.central.rpcservice.DubboReference;
import com.baimicro.central.user.IAppPermService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @ClassName AppPermController
 * @Description TODO
 * @Author baiHoo.chen
 * @Date 2020/3/26 11:12
 */
@RestController
@Slf4j
@RequestMapping("/perms")
public class AppPermController {

    @Resource
    private DubboReference dubboReference;

    @GetMapping("/{roleCodes}")
    public List<AppPerm> findPermByRoles(@PathVariable String roleCodes, String clientId) {
        List<AppPerm> result = null;
        if (StringUtils.isNotEmpty(roleCodes)) {
            IAppPermService appPermService = dubboReference.getAppPermService(clientId);
            result = appPermService.findPermissionByRoles(new HashSet<>(Arrays.asList(roleCodes.split(","))),
                    CommonConstant.PERMISSION);
        }
        return result;
    }
}
