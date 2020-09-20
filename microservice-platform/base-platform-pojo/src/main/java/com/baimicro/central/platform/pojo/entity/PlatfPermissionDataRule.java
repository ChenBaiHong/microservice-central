package com.baimicro.central.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Data
@TableName("platf_permission_data_rule")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlatfPermissionDataRule implements Serializable {

    /**
     * createBy
     */
    @Excel(name = "createBy", width = 15)
    private String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 菜单ID
     */
    @Excel(name = "菜单ID", width = 15)
    private Long permissionId;
    /**
     * 字段
     */
    @Excel(name = "字段", width = 15)
    private String ruleColumn;
    /**
     * 条件
     */
    @Excel(name = "条件", width = 15)
    private String ruleConditions;
    /**
     * 规则名称
     */
    @Excel(name = "规则名称", width = 15)
    private String ruleName;
    /**
     * 规则值
     */
    @Excel(name = "规则值", width = 15)
    private String ruleValue;
    /**
     * 权限有效状态1有0否
     */
    @Excel(name = "权限有效状态1有0否", width = 15)
    private String status;
    /**
     * 修改人
     */
    @Excel(name = "修改人", width = 15)
    private String updateBy;
    /**
     * 修改时间
     */
    @Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
