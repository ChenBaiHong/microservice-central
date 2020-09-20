package com.baimicro.central.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName AppPerm
 * @Description TODO
 * @Author baiHoo.chen
 * @Date 2020/3/20 16:08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AppPerm implements Serializable {

    private Long id;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单权限编码，例如：“sys:schedule:list,sys:schedule:info”,多个逗号隔开
     */
    private String perms;
    /**
     * 权限策略1显示2禁用
     */
    private String permsType;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 组件
     */
    private String component;

    /**
     * 组件名字
     */
    private String componentName;

    /**
     * 路径
     */
    private String url;
    /**
     * 一级菜单跳转地址
     */
    private String redirect;

    /**
     * 菜单排序
     */
    private Integer sortNo;

    /**
     * 类型（0：一级菜单；1：子菜单 ；2：按钮权限）
     */
    private Integer menuType;

    /**
     * 是否叶子节点: 1:是  0:不是
     */
    private boolean leaf;

    /**
     * 是否路由菜单: 0:不是  1:是（默认值1）
     */
    private boolean route;

    /**
     * 是否缓存页面: 0:不是  1:是（默认值1）
     */
    private boolean keepAlive;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建人
     */
    private String createBy;

    /**
     * 删除状态 0正常 1已删除
     */
    private Integer delFlag;
    /**
     * 是否配置菜单的数据权限 1是0否 默认0
     */
    private Integer ruleFlag;

    /**
     * 是否隐藏路由菜单: 0否,1是（默认值0）
     */
    private boolean hidden;

    /**
     * 按钮权限状态(0无效1有效)
     */
    private String status;

    /**
     * alwaysShow
     */
    private boolean alwaysShow;

    /**
     * 请求的类型
     */
    private String pathMethod;
    /**
     * 外链菜单打开方式 0/内部打开 1/外部打开
     */
    private boolean internalOrExternal;

}
