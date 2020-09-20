package com.baimicro.central.platform.system.service.impl;

import com.baimicro.central.common.model.Result;
import com.baimicro.central.platform.component.utils.ConvertUtils;
import com.baimicro.central.platform.pojo.constant.PlatformCacheConstant;
import com.baimicro.central.platform.pojo.constant.PlatformCommonConstant;
import com.baimicro.central.platform.pojo.entity.PlatfPermission;
import com.baimicro.central.platform.pojo.entity.PlatfUser;
import com.baimicro.central.platform.pojo.entity.PlatfUserRole;
import com.baimicro.central.platform.pojo.vo.UserCacheInfo;
import com.baimicro.central.platform.system.mapper.PlatfPermissionMapper;
import com.baimicro.central.platform.system.mapper.PlatfUserMapper;
import com.baimicro.central.platform.system.mapper.PlatfUserRoleMapper;
import com.baimicro.central.platform.system.service.IPlatfUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Service
@Slf4j
public class PlatfUserServiceImpl extends ServiceImpl<PlatfUserMapper, PlatfUser> implements IPlatfUserService {


    @Autowired
    private PlatfUserMapper userMapper;
    @Autowired
    private PlatfPermissionMapper platfPermissionMapper;
    @Autowired
    private PlatfUserRoleMapper platfUserRoleMapper;


    @Override
    public PlatfUser getUserByName(String username) {
        return userMapper.getUserByName(username);
    }


    @Override
    @Transactional
    public void addUserWithRole(PlatfUser user, String roles) {
        this.save(user);
        if (ConvertUtils.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                PlatfUserRole userRole = new PlatfUserRole(user.getId(), Long.parseLong(roleId));
                platfUserRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    @CacheEvict(value = PlatformCacheConstant.LOGIN_USER_RULES_CACHE, allEntries = true)
    @Transactional
    public void editUserWithRole(PlatfUser user, String roles) {
        this.updateById(user);
        //先删后加
        platfUserRoleMapper.delete(new QueryWrapper<PlatfUserRole>().lambda().eq(PlatfUserRole::getUserId, user.getId()));
        if (ConvertUtils.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                PlatfUserRole userRole = new PlatfUserRole(user.getId(), Long.parseLong(roleId));
                platfUserRoleMapper.insert(userRole);
            }
        }
    }


    @Override
    public List<String> getRole(String username) {
        return platfUserRoleMapper.getRoleByUserName(username);
    }

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    @Override
    @Cacheable(value = PlatformCacheConstant.LOGIN_USER_RULES_CACHE, key = "'Roles_'+#username")
    public Set<String> getUserRolesSet(String username) {
        // 查询用户拥有的角色集合
        List<String> roles = platfUserRoleMapper.getRoleByUserName(username);
        log.info("-------通过数据库读取用户拥有的角色Rules------username： " + username + ",Roles size: " + (roles == null ? 0 : roles.size()));
        return new HashSet<>(roles);
    }

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    @Override
    @Cacheable(value = PlatformCacheConstant.LOGIN_USER_RULES_CACHE, key = "'Permissions_'+#username")
    public Set<String> getUserPermissionsSet(String username) {
        Set<String> permissionSet = new HashSet<>();
        List<PlatfPermission> permissionList = platfPermissionMapper.selectPermByUsername(username);
        for (PlatfPermission po : permissionList) {
            if (ConvertUtils.isNotEmpty(po.getPerms())) {
                permissionSet.add(po.getPerms());
            }
        }
        log.info("-------通过数据库读取用户拥有的权限Perms------username： " + username + ",Perms size: " + (permissionSet == null ? 0 : permissionSet.size()));
        return permissionSet;
    }

    @Override
    public UserCacheInfo getCacheUser(String username) {
        UserCacheInfo info = new UserCacheInfo();
        PlatfUser user = getUserByName(username);
        if (user != null) {
            info.setUserCode(user.getUsername());
            info.setUserName(user.getRealname());
        }
        return info;
    }

    // 根据部门Id查询
    @Override
    public IPage<PlatfUser> getUserByDepId(Page<PlatfUser> page, Long departId, String username) {
        return userMapper.getUserByDepId(page, departId, username);
    }


    // 根据角色Id查询
    @Override
    public IPage<PlatfUser> getUserByRoleId(Page<PlatfUser> page, Long roleId, String username) {
        return userMapper.getUserByRoleId(page, roleId, username);
    }


    @Override
    public void updateUserDepart(String username, String orgCode) {
        baseMapper.updateUserDepart(username, orgCode);
    }


    @Override
    public PlatfUser getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }


    @Override
    public PlatfUser getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }


    /**
     * 校验用户是否有效
     *
     * @param platfUser
     * @return
     */
    @Override
    public Result<?> checkUserIsEffective(PlatfUser platfUser) {
        //情况1：根据用户信息查询，该用户不存在
        if (platfUser == null) {
            return Result.failed("该用户不存在，请注册");
        }
        //情况2：根据用户信息查询，该用户已注销
        if (PlatformCommonConstant.DEL_FLAG_1.toString().equals(platfUser.getDelFlag())) {
            return Result.failed("该用户已注销");
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (PlatformCommonConstant.USER_FREEZE.equals(platfUser.getEnabled())) {
            return Result.failed("该用户已冻结");
        }
        return Result.succeed();
    }
}
