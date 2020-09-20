package com.baimicro.central.platform.system.service;

import com.baimicro.central.common.model.Result;
import com.baimicro.central.platform.pojo.entity.PlatfUser;
import com.baimicro.central.platform.pojo.vo.UserCacheInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface IPlatfUserService extends IService<PlatfUser> {



    public PlatfUser getUserByName(String username);

    /**
     * 添加用户和用户角色关系
     *
     * @param user
     * @param roles
     */
    public void addUserWithRole(PlatfUser user, String roles);


    /**
     * 修改用户和用户角色关系
     *
     * @param user
     * @param roles
     */
    public void editUserWithRole(PlatfUser user, String roles);

    /**
     * 获取用户的授权角色
     *
     * @param username
     * @return
     */
    public List<String> getRole(String username);

    /**
     * 查询用户信息包括 部门信息
     *
     * @param username
     * @return
     */
    public UserCacheInfo getCacheUser(String username);

    /**
     * 根据部门Id查询
     *
     * @param
     * @return
     */
    public IPage<PlatfUser> getUserByDepId(Page<PlatfUser> page, Long departId, String username);

    /**
     * 根据角色Id查询
     *
     * @param
     * @return
     */
    public IPage<PlatfUser> getUserByRoleId(Page<PlatfUser> page, Long roleId, String username);

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    Set<String> getUserRolesSet(String username);

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    Set<String> getUserPermissionsSet(String username);

    /**
     * 根据用户名设置部门ID
     *
     * @param username
     * @param orgCode
     */
    void updateUserDepart(String username, String orgCode);

    /**
     * 根据手机号获取用户名和密码
     */
    public PlatfUser getUserByPhone(String phone);


    /**
     * 根据邮箱获取用户
     */
    public PlatfUser getUserByEmail(String email);

    /**
     * 校验用户是否有效
     *
     * @param user
     * @return
     */
    Result checkUserIsEffective(PlatfUser user);
}
