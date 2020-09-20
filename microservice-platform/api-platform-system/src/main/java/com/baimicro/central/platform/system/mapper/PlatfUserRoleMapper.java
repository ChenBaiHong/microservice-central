package com.baimicro.central.platform.system.mapper;

import com.baimicro.central.platform.pojo.entity.PlatfUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Mapper
public interface PlatfUserRoleMapper extends BaseMapper<PlatfUserRole> {

    @Select("select role_code from platf_role where id in (select role_id from platf_user_role where user_id = (select id from platf_user where username=#{username}))")
    List<String> getRoleByUserName(@Param("username") String username);
}
