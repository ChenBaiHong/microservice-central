package com.baimicro.central.config;

import com.baimicro.central.platform.pojo.entity.PlatfPermission;
import com.baimicro.central.platform.system.mapper.PlatfPermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ThePermission
 * @Description TODO 权限细粒度划分
 * @Author baiHoo.chen
 * @Date 2019/9/3 10:36
 */
@Component
@Slf4j
public class MyPermissionEvaluator implements PermissionEvaluator {
    @Resource
    private PlatfPermissionMapper platfPermissionMapper;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        String username = authentication.getName();
        boolean isAuth = false;
        if (StringUtils.isNotEmpty(username)) {
            String tperm;
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("perms", permission);
            List<PlatfPermission> permissions = platfPermissionMapper.selectPerms(map);
            for (PlatfPermission perm : permissions) {
                tperm = perm.getPerms();
                if (permission.equals(tperm)) {
                    isAuth = true;
                    break;
                }else {
                    if (tperm != null && tperm.contains(permission+"")) {
                        isAuth = true;
                        break;
                    }
                }
            }
        }
        return isAuth;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {

        return false;
    }
}
