package com.baimicro.central.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: jeecg-boot
 * @Date: 2020-03-25
 * @Version: V1.0
 */
@Data
@TableName("platf_permission")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlatfPermission implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 父id
     */
    private Integer parentId;
    /**
     * 菜单标题
     */
    private String name;
    /**
     * 路径
     */

    private String url;
    /**
     * 组件
     */

    private String component;
    /**
     * 组件名字
     */

    private String componentName;
    /**
     * 一级菜单跳转地址
     */

    private String redirect;
    /**
     * 菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)
     */

    private Integer menuType;
    /**
     * 菜单权限编码
     */

    private String perms;
    /**
     * 权限策略1显示2禁用
     */

    private Integer permsType;
    /**
     * 菜单排序
     */

    private Integer sortNo;
    /**
     * 聚合子路由: 1是0否
     */

    private Integer alwaysShow;
    /**
     * 菜单图标
     */

    private String icon;
    /**
     * 是否路由菜单: 0:不是  1:是（默认值1）
     */

    private Integer isRoute;
    /**
     * 是否叶子节点:    1:是   0:不是
     */

    private Integer isLeaf;
    /**
     * 是否缓存该页面:    1:是   0:不是
     */

    private Integer keepAlive;
    /**
     * 是否隐藏路由: 0否,1是
     */

    private Integer hidden;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 删除状态 0正常 1已删除
     */
    private Integer delFlag;

    /**
     * 是否添加数据权限1是0否
     */
    private Integer ruleFlag;

    /**
     * 按钮权限状态(0无效1有效)
     */
    private String status;

    /**
     * 外链菜单打开方式 0/内部打开 1/外部打开
     */
    private boolean internalOrExternal;
}
