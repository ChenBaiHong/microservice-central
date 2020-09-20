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
@TableName("platf_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlatfUser implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 登录账号
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 密码
     */
    private String password;
    /**
     * md5密码盐
     */
    private String salt;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 生日
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date birthday;
    /**
     * 性别
     */
    private String sex;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 电话
     */
    private String phone;
    /**
     * 机构编码
     */
    private String orgCode;
    /**
     * 性别(1-正常,0冻结)
     */
    private Integer enabled;
    /**
     * 删除状态(0-正常,1-已删除)
     */
    private Integer delFlag;
    /**
     * 同步工作流引擎(1-同步,0-不同步)
     */
    private Integer activitiSync;
    /**
     * 工号，唯一键
     */
    private String workNo;
    /**
     * 职务，关联职务表
     */
    private String post;
    /**
     * 座机号
     */
    private String telephone;
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
}
