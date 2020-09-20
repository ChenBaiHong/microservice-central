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
@TableName("sys_dict_item")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDictItem implements Serializable {

    /**
     * createBy
     */
    @Excel(name = "createBy", width = 15)
    private String createBy;
    /**
     * createTime
     */
    @Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    private String description;
    /**
     * 字典id
     */
    @Excel(name = "字典id", width = 15)
    private Long dictId;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 字典项文本
     */
    @Excel(name = "字典项文本", width = 15)
    private String itemText;
    /**
     * 字典项值
     */
    @Excel(name = "字典项值", width = 15)
    private String itemValue;
    /**
     * 排序
     */
    @Excel(name = "排序", width = 15)
    private Integer sortOrder;
    /**
     * 状态（1启用 0不启用）
     */
    @Excel(name = "状态（1启用 0不启用）", width = 15)
    private Integer status;
    /**
     * updateBy
     */
    @Excel(name = "updateBy", width = 15)
    private String updateBy;
    /**
     * updateTime
     */
    @Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
