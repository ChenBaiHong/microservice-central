package com.baimicro.central.platform.system.service.impl;

import com.baimicro.central.common.service.impl.SuperServiceImpl;
import com.baimicro.central.platform.component.utils.ConvertUtils;
import com.baimicro.central.platform.component.utils.PlatformExcepUtil;
import com.baimicro.central.platform.pojo.constant.PlatformCacheConstant;
import com.baimicro.central.platform.pojo.constant.PlatformCommonConstant;
import com.baimicro.central.platform.pojo.entity.PlatfPermission;
import com.baimicro.central.platform.pojo.entity.PlatfPermissionDataRule;
import com.baimicro.central.platform.pojo.model.platform.TreeModel;
import com.baimicro.central.platform.system.mapper.PlatfPermissionMapper;
import com.baimicro.central.platform.system.service.IPlatfPermissionDataRuleService;
import com.baimicro.central.platform.system.service.IPlatfPermissionService;
import com.baimicro.central.redis.manager.CacheExpire;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Service
public class PlatfPermissionServiceImpl extends SuperServiceImpl<PlatfPermissionMapper, PlatfPermission> implements IPlatfPermissionService {

    @Resource
    private PlatfPermissionMapper platfPermissionMapper;

    @Resource
    private IPlatfPermissionDataRuleService permissionDataRuleService;

    @Override
    public List<TreeModel> queryListByParentId(Long parentId) {
        return platfPermissionMapper.queryListByParentId(parentId);
    }

    /**
     * 真实删除
     */
    @Override
    @Transactional
    @CacheEvict(value = PlatformCacheConstant.PLATF_PERMISSION_SERVICE, allEntries = true)
    public void deletePermission(Long id) {
        PlatfPermission platfPermission = this.getById(id);
        if (platfPermission == null) {
            throw PlatformExcepUtil.throwBusinessException("未找到菜单信息");
        }
        Long pid = platfPermission.getParentId();
        if (ConvertUtils.isNotEmpty(pid)) {
            int count = this.count(new QueryWrapper<PlatfPermission>().lambda().eq(PlatfPermission::getParentId, pid));
            if (count == 1) {
                //若父节点无其他子节点，则该父节点是叶子节点
                this.platfPermissionMapper.setMenuLeaf(pid, 1);
            }
        }
        platfPermissionMapper.deleteById(id);
        // 该节点可能是子节点但也可能是其它节点的父节点,所以需要级联删除
        this.removeChildrenBy(platfPermission.getId());
    }

    /**
     * 根据父id删除其关联的子节点数据
     *
     * @return
     */
    public void removeChildrenBy(Long parentId) {
        LambdaQueryWrapper<PlatfPermission> query = new LambdaQueryWrapper<>();
        // 封装查询条件parentId为主键,
        query.eq(PlatfPermission::getParentId, parentId);
        // 查出该主键下的所有子级
        List<PlatfPermission> permissionList = this.list(query);
        if (permissionList != null && permissionList.size() > 0) {
            Long id; // id
            int num = 0; // 查出的子级数量
            // 如果查出的集合不为空, 则先删除所有
            this.remove(query);
            // 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
            for (int i = 0, len = permissionList.size(); i < len; i++) {
                id = permissionList.get(i).getId();
                num = this.count(new LambdaQueryWrapper<PlatfPermission>().eq(PlatfPermission::getParentId, id));
                // 如果有, 则递归
                if (num > 0) {
                    this.removeChildrenBy(id);
                }
            }
        }
    }

    /**
     * 逻辑删除
     */
    @Override
    @CacheEvict(value = PlatformCacheConstant.PLATF_PERMISSION_SERVICE, allEntries = true)
    public void deletePermissionLogical(Long id) {
        PlatfPermission platfPermission = this.getById(id);
        if (platfPermission == null) {
            throw PlatformExcepUtil.throwBusinessException("未找到菜单信息");
        }
        Long pid = platfPermission.getParentId();
        int count = this.count(new QueryWrapper<PlatfPermission>().lambda().eq(PlatfPermission::getParentId, pid));
        if (count == 1) {
            //若父节点无其他子节点，则该父节点是叶子节点
            this.platfPermissionMapper.setMenuLeaf(pid, 1);
        }
        platfPermission.setDelFlag(1);
        this.updateById(platfPermission);
    }

    @Override
    @CacheEvict(value = PlatformCacheConstant.PLATF_PERMISSION_SERVICE, allEntries = true)
    public void addPermission(PlatfPermission platfPermission) {
        //----------------------------------------------------------------------
        //判断是否是一级菜单，是的话清空父菜单
        if (PlatformCommonConstant.MENU_TYPE_0.equals(platfPermission.getMenuType())) {
            platfPermission.setParentId(null);
        }
        //----------------------------------------------------------------------
        Long pid = platfPermission.getParentId();
        if (ConvertUtils.isNotEmpty(pid)) {
            //设置父节点不为叶子节点
            this.platfPermissionMapper.setMenuLeaf(pid, 0);
        }
        platfPermission.setCreateTime(LocalDateTime.now());
        platfPermission.setDelFlag(0);
        platfPermission.setLeaf(true);
        this.save(platfPermission);
    }

    @Override
    @CacheEvict(value = PlatformCacheConstant.PLATF_PERMISSION_SERVICE, allEntries = true)
    public void editPermission(PlatfPermission platfPermission) {
        PlatfPermission p = this.getById(platfPermission.getId());
        //TODO 该节点判断是否还有子节点
        if (p == null) {
            throw PlatformExcepUtil.throwBusinessException("未找到菜单信息");
        } else {
            platfPermission.setUpdateTime(LocalDateTime.now());
            //----------------------------------------------------------------------
            //Step1.判断是否是一级菜单，是的话清空父菜单ID
            if (PlatformCommonConstant.MENU_TYPE_0.equals(platfPermission.getMenuType())) {
                platfPermission.setParentId(null);
            }
            //Step2.判断菜单下级是否有菜单，无则设置为叶子节点
            int count = this.count(new QueryWrapper<PlatfPermission>().lambda().eq(PlatfPermission::getParentId, platfPermission.getId()));
            if (count == 0) {
                platfPermission.setLeaf(true);
            }
            //----------------------------------------------------------------------
            this.updateById(platfPermission);

            //如果当前菜单的父菜单变了，则需要修改新父菜单和老父菜单的，叶子节点状态
            Long pid = platfPermission.getParentId();
            if ((ConvertUtils.isNotEmpty(pid) && !pid.equals(p.getParentId())) || ConvertUtils.isEmpty(pid) && ConvertUtils.isNotEmpty(p.getParentId())) {
                //a.设置新的父菜单不为叶子节点
                this.platfPermissionMapper.setMenuLeaf(pid, 0);
                //b.判断老的菜单下是否还有其他子菜单，没有的话则设置为叶子节点
                int cc = this.count(new QueryWrapper<PlatfPermission>().lambda().eq(PlatfPermission::getParentId, p.getParentId()));
                if (cc == 0) {
                    if (ConvertUtils.isNotEmpty(p.getParentId())) {
                        this.platfPermissionMapper.setMenuLeaf(p.getParentId(), 1);
                    }
                }

            }
        }

    }

    @Override
    public List<PlatfPermission> queryByUser(String username) {
        return this.platfPermissionMapper.selectPermByUsername(username);
    }

    /**
     * 根据permissionId删除其关联的PlatfPermissionDataRule表中的数据
     */
    @Override
    public void deletePermRuleByPermId(Long id) {
        LambdaQueryWrapper<PlatfPermissionDataRule> query = new LambdaQueryWrapper<>();
        query.eq(PlatfPermissionDataRule::getPermissionId, id);
        int countValue = this.permissionDataRuleService.count(query);
        if (countValue > 0) {
            this.permissionDataRuleService.remove(query);
        }
    }

    /**
     * 获取模糊匹配规则的数据权限URL
     */
    @Override
    @Cacheable(key = "'queryPermissionUrlWithStar'"
            , value = PlatformCacheConstant.PLATF_PERMISSION_SERVICE
            , unless = "#result eq null or #result.size() eq 0")
    @CacheExpire(60 * 60 * 2) // 缓存两小时
    public List<String> queryPermissionUrlWithStar() {
        return this.baseMapper.queryPermissionUrlWithStar();
    }

}
