package com.baimicro.central.platform.system.service.impl;


import com.baimicro.central.common.service.impl.SuperServiceImpl;
import com.baimicro.central.platform.component.utils.ConvertUtils;
import com.baimicro.central.platform.pojo.constant.PlatformCacheConstant;
import com.baimicro.central.platform.pojo.entity.PlatfRolePermission;
import com.baimicro.central.platform.system.mapper.PlatfRolePermissionMapper;
import com.baimicro.central.platform.system.service.IPlatfRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Service
public class PlatfRolePermissionServiceImpl extends SuperServiceImpl<PlatfRolePermissionMapper, PlatfRolePermission> implements IPlatfRolePermissionService {

    @Override
    @CacheEvict(value = PlatformCacheConstant.LOGIN_USER_RULES_CACHE, allEntries = true)
    public void saveRolePermission(Long roleId, String permissionIds) {
        LambdaQueryWrapper<PlatfRolePermission> query = new QueryWrapper<PlatfRolePermission>().lambda().eq(PlatfRolePermission::getRoleId, roleId);
        this.remove(query);
        List<PlatfRolePermission> list = new ArrayList<PlatfRolePermission>();
        String[] arr = permissionIds.split(",");
        for (String p : arr) {
            if (ConvertUtils.isNotEmpty(p)) {
                PlatfRolePermission rolepms = new PlatfRolePermission(roleId, Long.parseLong(p));
                list.add(rolepms);
            }
        }

        this.saveBatch(list);
    }

    @Override
    @CacheEvict(value = PlatformCacheConstant.LOGIN_USER_RULES_CACHE, allEntries = true)
    public void saveRolePermission(Long roleId, String permissionIds, String lastPermissionIds) {
        List<String> add = getDiff(lastPermissionIds, permissionIds);
        if (add != null && add.size() > 0) {
            List<PlatfRolePermission> list = new ArrayList<PlatfRolePermission>();
            for (String p : add) {
                if (ConvertUtils.isNotEmpty(p)) {
                    PlatfRolePermission rolepms = new PlatfRolePermission(roleId, Long.parseLong(p));
                    list.add(rolepms);
                }
            }
            this.saveBatch(list);
        }

        List<String> delete = getDiff(permissionIds, lastPermissionIds);
        if (delete != null && delete.size() > 0) {
            for (String permissionId : delete) {
                this.remove(new QueryWrapper<PlatfRolePermission>().lambda().eq(PlatfRolePermission::getRoleId, roleId).eq(PlatfRolePermission::getPermissionId, permissionId));
            }
        }
    }

    /**
     * 从diff中找出main中没有的元素
     *
     * @param main
     * @param diff
     * @return
     */
    private List<String> getDiff(String main, String diff) {
        if (ConvertUtils.isEmpty(diff)) {
            return null;
        }
        if (ConvertUtils.isEmpty(main)) {
            return Arrays.asList(diff.split(","));
        }

        String[] mainArr = main.split(",");
        String[] diffArr = diff.split(",");
        Map<String, Integer> map = new HashMap<>();
        for (String string : mainArr) {
            map.put(string, 1);
        }
        List<String> res = new ArrayList<String>();
        for (String key : diffArr) {
            if (ConvertUtils.isNotEmpty(key) && !map.containsKey(key)) {
                res.add(key);
            }
        }
        return res;
    }
}
