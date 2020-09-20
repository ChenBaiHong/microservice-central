package com.baimicro.central.platform.system.service;


import com.baimicro.central.common.service.ISuperService;
import com.baimicro.central.platform.pojo.entity.PlatfPermissionDataRule;

import java.util.List;

/**
 * @Description: 菜单权限规则 服务类
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface IPlatfPermissionDataRuleService extends ISuperService<PlatfPermissionDataRule> {

    /**
     * 根据菜单id查询其对应的权限数据
     *
     * @param permRule
     */
    List<PlatfPermissionDataRule> getPermRuleListByPermId(Long permissionId);

    /**
     * 根据页面传递的参数查询菜单权限数据
     *
     * @return
     */
    List<PlatfPermissionDataRule> queryPermissionRule(PlatfPermissionDataRule permRule);


    /**
     * 根据菜单ID和用户名查找数据权限配置信息
     *
     * @param permission
     * @param username
     * @return
     */
    List<PlatfPermissionDataRule> queryPermissionDataRules(String username, Long permissionId);

    /**
     * 新增菜单权限配置 修改菜单rule_flag
     *
     * @param sysPermissionDataRule
     */
    public void savePermissionDataRule(PlatfPermissionDataRule permissionDataRule);

    /**
     * 删除菜单权限配置 判断菜单还有无权限
     *
     * @param dataRuleId
     */
    public void deletePermissionDataRule(Long dataRuleId);
}
