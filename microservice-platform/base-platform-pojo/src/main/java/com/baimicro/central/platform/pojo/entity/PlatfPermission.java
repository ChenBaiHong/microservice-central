package com.baimicro.central.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
@TableName("platf_permission")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlatfPermission implements Serializable {

    /**
     * 聚合子路由: 1是0否
     */
    @Excel(name = "聚合子路由: 1是0否", width = 15)
    private boolean alwaysShow;
    /**
     * 组件
     */
    @Excel(name = "组件", width = 15)
    private String component;
    /**
     * 组件名字
     */
    @Excel(name = "组件名字", width = 15)
    private String componentName;
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
     * 删除状态 0正常 1已删除
     */
    @Excel(name = "删除状态 0正常 1已删除", width = 15)
    private Integer delFlag;
    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    private String description;
    /**
     * 是否隐藏路由: 0否,1是
     */
    @Excel(name = "是否隐藏路由: 0否,1是", width = 15)
    private boolean hidden;
    /**
     * 菜单图标
     */
    @Excel(name = "菜单图标", width = 15)
    private String icon;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 外链菜单打开方式 0/内部打开 1/外部打开
     */
    @Excel(name = "外链菜单打开方式 0/内部打开 1/外部打开", width = 15)
    private boolean internalOrExternal;
    /**
     * 是否叶子节点:    1:是   0:不是
     */
    @Excel(name = "是否叶子节点:    1:是   0:不是", width = 15)
    private boolean isLeaf;
    /**
     * 是否路由菜单: 0:不是  1:是（默认值1）
     */
    @Excel(name = "是否路由菜单: 0:不是  1:是（默认值1）", width = 15)
    private boolean isRoute;
    /**
     * 是否缓存该页面:    1:是   0:不是
     */
    @Excel(name = "是否缓存该页面:    1:是   0:不是", width = 15)
    private boolean keepAlive;
    /**
     * 菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)
     */
    @Excel(name = "菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)", width = 15)
    private Integer menuType;
    /**
     * 菜单标题
     */
    @Excel(name = "菜单标题", width = 15)
    private String name;
    /**
     * 父id
     */
    @Excel(name = "父id", width = 15)
    private Long parentId;
    /**
     * 菜单权限编码
     */
    @Excel(name = "菜单权限编码", width = 15)
    private String perms;
    /**
     * 权限策略1显示2禁用
     */
    @Excel(name = "权限策略1显示2禁用", width = 15)
    private Integer permsType;
    /**
     * 一级菜单跳转地址
     */
    @Excel(name = "一级菜单跳转地址", width = 15)
    private String redirect;
    /**
     * 是否添加数据权限1是0否
     */
    @Excel(name = "是否添加数据权限1是0否", width = 15)
    private Integer ruleFlag;
    /**
     * 菜单排序
     */
    @Excel(name = "菜单排序", width = 15)
    private Integer sortNo;
    /**
     * 按钮权限状态(0无效1有效)
     */
    @Excel(name = "按钮权限状态(0无效1有效)", width = 15)
    private String status;
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
     * 路径
     */
    @Excel(name = "路径", width = 15)
    private String url;
    /**
     * 租户ID
     */
    @Excel(name = "租户 ID", width = 15)
    private String tenantId;

    public PlatfPermission() {
    }

    public PlatfPermission(boolean index) {
        if (index) {
            this.id = 0L;
            this.name = "首页";
            this.component = "dashboard/Analysis";
            this.url = "/dashboard/analysis";
            this.icon = "home";
            this.menuType = 0;
            this.sortNo = 0;
            this.ruleFlag = 0;
            this.delFlag = 0;
            this.alwaysShow = false;
            this.isRoute = true;
            this.keepAlive = true;
            this.isLeaf = true;
            this.hidden = false;
        }
    }
}
