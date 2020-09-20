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
@TableName("platf_role")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlatfRole implements Serializable {

    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    private String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    private String description;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色编码
     */
    @Excel(name = "角色编码", width = 15)
    private String roleCode;
    /**
     * 角色名称
     */
    @Excel(name = "角色名称", width = 15)
    private String roleName;
    /**
     * 更新人
     */
    @Excel(name = "更新人", width = 15)
    private String updateBy;
    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 租户ID
     */
    @Excel(name = "租户 ID", width = 15)
    private String tenantId;
}
