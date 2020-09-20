package com.baimicro.central.platform.system.service;


import com.baimicro.central.common.service.ISuperService;
import com.baimicro.central.platform.pojo.entity.PlatfRolePermission;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface IPlatfRolePermissionService extends ISuperService<PlatfRolePermission> {
    /**
     * 保存授权/先删后增
     *
     * @param roleId
     * @param permissionIds
     */
    public void saveRolePermission(Long roleId, String permissionIds);

    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     *
     * @param roleId
     * @param permissionIds
     * @param lastPermissionIds
     */
    public void saveRolePermission(Long roleId, String permissionIds, String lastPermissionIds);
}
