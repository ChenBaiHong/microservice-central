package com.baimicro.central.user.mapper;

import com.baimicro.central.common.model.AppUser;
import com.baimicro.central.user.entity.PlatfUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/**
 * @Description:
 * @Author: jeecg-boot
 * @Date: 2020-03-25
 * @Version: V1.0
 */
@Mapper
public interface PlatfUserMapper extends BaseMapper<PlatfUser> {

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    AppUser selectByUsername(@Param("username") String username);

    /**
     * 根据手机号查询用户
     *
     * @param mobile
     * @return
     */
    AppUser selectByMobile(@Param("mobile") String mobile);

    /**
     * 根据openId查询用户
     *
     * @param openId
     * @return
     */
    AppUser selectByOpenId(@Param("openId") String openId);

}
