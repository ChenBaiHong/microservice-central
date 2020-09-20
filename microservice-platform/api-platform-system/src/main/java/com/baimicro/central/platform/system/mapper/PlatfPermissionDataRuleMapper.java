package com.baimicro.central.platform.system.mapper;

import com.baimicro.central.platform.pojo.entity.PlatfPermissionDataRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Mapper
public interface PlatfPermissionDataRuleMapper extends BaseMapper<PlatfPermissionDataRule> {
    /**
     * 根据用户名和权限id查询
     *
     * @param username
     * @param permissionId
     * @return
     */
    public List<String> queryDataRuleIds(@Param("username") String username, @Param("permissionId") Long permissionId);


}
