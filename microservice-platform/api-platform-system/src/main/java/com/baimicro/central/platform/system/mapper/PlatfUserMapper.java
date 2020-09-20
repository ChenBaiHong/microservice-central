package com.baimicro.central.platform.system.mapper;

import com.baimicro.central.platform.pojo.entity.PlatfUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Mapper
public interface PlatfUserMapper extends BaseMapper<PlatfUser> {

    /**
     * 通过用户账号查询用户信息
     *
     * @param username
     * @return
     */
    public PlatfUser getUserByName(@Param("username") String username);


    /**
     * 根据部门Id查询用户信息
     *
     * @param page
     * @param departId
     * @return
     */
    IPage<PlatfUser> getUserByDepId(Page page, @Param("departId") Long departId, @Param("username") String username);

    /**
     * 根据角色Id查询用户信息
     *
     * @param page
     * @param
     * @return
     */
    IPage<PlatfUser> getUserByRoleId(Page page, @Param("roleId") Long roleId, @Param("username") String username);

    /**
     * 根据用户名设置部门ID
     *
     * @param username
     * @param departId
     */
    void updateUserDepart(@Param("username") String username, @Param("orgCode") String orgCode);

    /**
     * 根据手机号查询用户信息
     *
     * @param phone
     * @return
     */
    public PlatfUser getUserByPhone(@Param("phone") String phone);


    /**
     * 根据邮箱查询用户信息
     *
     * @param email
     * @return
     */
    public PlatfUser getUserByEmail(@Param("email") String email);
}
