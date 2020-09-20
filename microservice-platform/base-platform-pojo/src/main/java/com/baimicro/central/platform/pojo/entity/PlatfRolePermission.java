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
@TableName("platf_role_permission")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlatfRolePermission {

    /**
     * dataRuleIds
     */
    @Excel(name = "dataRuleIds", width = 15)
    private String dataRuleIds;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 权限id
     */
    @Excel(name = "权限id", width = 15)
    private Long permissionId;
    /**
     * 角色id
     */
    @Excel(name = "角色id", width = 15)
    private Long roleId;

    public PlatfRolePermission() {
    }

    public PlatfRolePermission(Long roleId, Long permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}
