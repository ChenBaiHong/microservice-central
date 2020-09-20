package com.baimicro.central.platform.system.service.impl;


import com.baimicro.central.common.service.impl.SuperServiceImpl;
import com.baimicro.central.platform.component.query.QueryGenerator;
import com.baimicro.central.platform.component.utils.ConvertUtils;
import com.baimicro.central.platform.pojo.constant.PlatformCommonConstant;
import com.baimicro.central.platform.pojo.entity.PlatfPermission;
import com.baimicro.central.platform.pojo.entity.PlatfPermissionDataRule;
import com.baimicro.central.platform.system.mapper.PlatfPermissionDataRuleMapper;
import com.baimicro.central.platform.system.mapper.PlatfPermissionMapper;
import com.baimicro.central.platform.system.service.IPlatfPermissionDataRuleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Service
public class PlatfPermissionDataRuleServiceImpl extends SuperServiceImpl<PlatfPermissionDataRuleMapper, PlatfPermissionDataRule> implements IPlatfPermissionDataRuleService {

    @Resource
    private PlatfPermissionMapper platfPermissionMapper;

    /**
     * 根据菜单id查询其对应的权限数据
     */
    @Override
    public List<PlatfPermissionDataRule> getPermRuleListByPermId(Long permissionId) {
        LambdaQueryWrapper<PlatfPermissionDataRule> query = new LambdaQueryWrapper<PlatfPermissionDataRule>();
        query.eq(PlatfPermissionDataRule::getPermissionId, permissionId);
        query.orderByDesc(PlatfPermissionDataRule::getCreateTime);
        List<PlatfPermissionDataRule> permRuleList = this.list(query);
        return permRuleList;
    }

    /**
     * 根据前端传递的权限名称和权限值参数来查询权限数据
     */
    @Override
    public List<PlatfPermissionDataRule> queryPermissionRule(PlatfPermissionDataRule permRule) {
        QueryWrapper<PlatfPermissionDataRule> queryWrapper = QueryGenerator.initQueryWrapper(permRule, null);
        return this.list(queryWrapper);
    }

    @Override
    public List<PlatfPermissionDataRule> queryPermissionDataRules(String username, Long permissionId) {
        List<String> idsList = this.baseMapper.queryDataRuleIds(username, permissionId);
        if (idsList == null || idsList.size() == 0 || idsList.get(0) == null) {
            return null;
        }
        Set<String> set = new HashSet<String>();
        for (String ids : idsList) {
            if (ids == null) {
                continue;
            }
            String[] arr = ids.split(",");
            for (String id : arr) {
                if (ConvertUtils.isNotEmpty(id)) {
                    set.add(id);
                }
            }
        }
        if (set.size() == 0) {
            return null;
        }
        return this.baseMapper.selectList(new QueryWrapper<PlatfPermissionDataRule>().in("id", set).eq("status", PlatformCommonConstant.STATUS_1));
    }

    @Override
    @Transactional
    public void savePermissionDataRule(PlatfPermissionDataRule platfPermissionDataRule) {
        this.save(platfPermissionDataRule);
        PlatfPermission permission = platfPermissionMapper.selectById(platfPermissionDataRule.getPermissionId());
        if (permission != null && (permission.getRuleFlag() == null || permission.getRuleFlag().equals(PlatformCommonConstant.RULE_FLAG_0))) {
            permission.setRuleFlag(PlatformCommonConstant.RULE_FLAG_1);
            platfPermissionMapper.updateById(permission);
        }
    }

    @Override
    @Transactional
    public void deletePermissionDataRule(Long dataRuleId) {
        PlatfPermissionDataRule dataRule = this.baseMapper.selectById(dataRuleId);
        if (dataRule != null) {
            this.removeById(dataRuleId);
            Integer count = this.baseMapper.selectCount(new LambdaQueryWrapper<PlatfPermissionDataRule>().eq(PlatfPermissionDataRule::getPermissionId, dataRule.getPermissionId()));
            //注:同一个事务中删除后再查询是会认为数据已被删除的 若事务回滚上述删除无效
            if (count == null || count == 0) {
                PlatfPermission permission = platfPermissionMapper.selectById(dataRule.getPermissionId());
                if (permission != null && permission.getRuleFlag().equals(PlatformCommonConstant.RULE_FLAG_1)) {
                    permission.setRuleFlag(PlatformCommonConstant.RULE_FLAG_0);
                    platfPermissionMapper.updateById(permission);
                }
            }
        }

    }
}
