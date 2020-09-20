package com.baimicro.central.platform.system.service.impl;

import com.baimicro.central.platform.pojo.entity.PlatfRole;
import com.baimicro.central.platform.pojo.entity.PlatfUser;
import com.baimicro.central.platform.pojo.entity.PlatfUserRole;
import com.baimicro.central.platform.system.mapper.PlatfUserRoleMapper;
import com.baimicro.central.platform.system.service.IPlatfRoleService;
import com.baimicro.central.platform.system.service.IPlatfUserRoleService;
import com.baimicro.central.platform.system.service.IPlatfUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Service
public class PlatfUserRoleServiceImpl extends ServiceImpl<PlatfUserRoleMapper, PlatfUserRole> implements IPlatfUserRoleService {

    @Autowired
    private IPlatfUserService userService;
    @Autowired
    private IPlatfRoleService roleService;

    /**
     * 查询所有用户对应的角色信息
     */
    @Override
    public Map<Long, String> queryUserRole() {
        List<PlatfUserRole> uRoleList = this.list();
        List<PlatfUser> userList = userService.list();
        List<PlatfRole> roleList = roleService.list();
        Map<Long, String> map = new IdentityHashMap<>();
        Long userId;
        Long roleId;
        String roleName;
        if (uRoleList != null && uRoleList.size() > 0) {
            for (PlatfUserRole uRole : uRoleList) {
                roleId = uRole.getRoleId();
                for (PlatfUser user : userList) {
                    userId = user.getId();
                    if (uRole.getUserId().equals(userId)) {
                        roleName = this.searchByRoleId(roleList, roleId);
                        map.put(userId, roleName);
                    }
                }
            }
            return map;
        }
        return map;
    }

    /**
     * queryUserRole调用的方法
     *
     * @param roleList
     * @param roleId
     * @return
     */
    private String searchByRoleId(List<PlatfRole> roleList, Long roleId) {
        while (true) {
            for (PlatfRole role : roleList) {
                if (roleId.equals(role.getId())) {
                    return role.getRoleName();
                }
            }
        }
    }
}
