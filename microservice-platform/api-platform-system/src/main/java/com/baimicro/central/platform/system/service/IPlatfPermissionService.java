package com.baimicro.central.platform.system.service;

import com.baimicro.central.common.service.ISuperService;
import com.baimicro.central.platform.pojo.entity.PlatfPermission;
import com.baimicro.central.platform.pojo.model.platform.TreeModel;

import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
public interface IPlatfPermissionService extends ISuperService<PlatfPermission> {

    public List<TreeModel> queryListByParentId(Long parentId);

    /**
     * 真实删除
     */
    public void deletePermission(Long id);

    /**
     * 逻辑删除
     */
    public void deletePermissionLogical(Long id);

    public void addPermission(PlatfPermission platfPermission);

    public void editPermission(PlatfPermission platfPermission);

    public List<PlatfPermission> queryByUser(String username);

    /**
     * 根据permissionId删除其关联的PlatfPermissionDataRule表中的数据
     *
     * @param id
     * @return
     */
    public void deletePermRuleByPermId(Long id);

    /**
     * 查询出带有特殊符号的菜单地址的集合
     *
     * @return
     */
    public List<String> queryPermissionUrlWithStar();
}
