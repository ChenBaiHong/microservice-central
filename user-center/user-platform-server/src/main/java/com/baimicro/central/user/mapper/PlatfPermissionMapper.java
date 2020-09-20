package com.baimicro.central.user.mapper;

import com.baimicro.central.common.model.AppPerm;
import com.baimicro.central.user.entity.PlatfPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-03-25
 * @Version: V1.0
 */
@Mapper
public interface PlatfPermissionMapper extends BaseMapper<PlatfPermission> {

    List<AppPerm> findSimplePermissionByRoleIds(@Param("roleIds") Set<Serializable> roleIds);

    List<AppPerm> findAllPermissionByRoleIds(@Param("roleIds") Set<Serializable> roleIds);

    List<AppPerm> findSimplePermissionByRoleCodes(@Param("roleCodes") Set<Serializable> roleCodes);

    List<AppPerm> findAllPermissionByRoleCodes(@Param("roleCodes") Set<Serializable> roleCodes);
}
