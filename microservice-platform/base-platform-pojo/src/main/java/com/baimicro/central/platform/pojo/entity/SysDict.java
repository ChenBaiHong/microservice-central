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
@TableName("sys_dict")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDict implements Serializable {


    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    private String description;
    /**
     * 字典编码
     */
    @Excel(name = "字典编码", width = 15)
    private String dictCode;
    /**
     * 字典名称
     */
    @Excel(name = "字典名称", width = 15)
    private String dictName;

    /**
     * 删除状态
     */
    @Excel(name = "删除状态", width = 15)
    private Integer delFlag;
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
     * 字典类型0为string,1为number
     */
    @Excel(name = "字典类型0为string,1为number", width = 15)
    private Integer type;

}
