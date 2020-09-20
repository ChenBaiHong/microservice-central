package com.baimicro.central.user.mapper;

import com.baimicro.central.common.model.AppRole;
import com.baimicro.central.user.entity.PlatfRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-03-25
 * @Version: V1.0
 */
@Mapper
public interface PlatfRoleMapper extends BaseMapper<PlatfRole> {


    List<AppRole> findRolesByUserId(Serializable userId);
}
