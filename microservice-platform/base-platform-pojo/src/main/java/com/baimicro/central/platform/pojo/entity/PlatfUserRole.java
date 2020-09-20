package com.baimicro.central.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Data
@TableName("platf_user_role")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlatfUserRole {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色id
     */
    @Excel(name = "角色id", width = 15)
    private Long roleId;
    /**
     * 用户id
     */
    @Excel(name = "用户id", width = 15)
    private Long userId;

    public PlatfUserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
