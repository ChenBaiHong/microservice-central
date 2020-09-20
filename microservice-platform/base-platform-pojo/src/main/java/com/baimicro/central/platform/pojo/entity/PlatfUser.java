package com.baimicro.central.platform.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Data
@TableName("platf_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlatfUser implements Serializable {


    /**
     * 头像
     */
    @Excel(name = "头像", width = 15)
    private String avatar;

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
     * 删除状态(0-正常,1-已删除)
     */
    @Excel(name = "删除状态(0-正常,1-已删除)", width = 15)
    private Integer delFlag;
    /**
     * 电子邮件
     */
    @Excel(name = "电子邮件", width = 15)
    private String email;
    /**
     * 身份状态(1-正常,0-冻结)
     */
    @Excel(name = "身份状态(1-正常,0-冻结)", width = 15)
    private Integer enabled;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 密码
     */
    @Excel(name = "密码", width = 15)
    private String password;
    /**
     * 电话
     */
    @Excel(name = "电话", width = 15)
    private String phone;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名", width = 15)
    private String realname;

    /**
     * 性别(0-默认未知,1-男,2-女)
     */
    @Excel(name = "性别", width = 15)
    private String sex;

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
     * 登录账号
     */
    @Excel(name = "登录账号", width = 15)
    private String username;

    /**
     * 租户ID
     */
    @Excel(name = "租户 ID", width = 15)
    private String tenantId;

    /**
     * @Description: 对密码 BCrypt 加密
     * @Author: chen.baihoo
     * @Date: 2019/4/14
     */
    @Ignore
    public void setPasswordEncoder(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePasswd = bCryptPasswordEncoder.encode(password);
        this.password = encodePasswd;
    }

    /***
     *
     * @Author baihoo.chen
     * @Description TODO 前端传值密码，后端加密密码
     * @Date 2019/9/3 11:42
     * @Param [rowPassword, encoderPassword]
     * @return boolean
     **/
    @Ignore
    public boolean isPasswordMatches(String rowPassword, String encoderPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean result = bCryptPasswordEncoder.matches(rowPassword, encoderPassword);
        return result;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePasswd = bCryptPasswordEncoder.encode("artLangdon!@#321");
        System.out.println(encodePasswd);
    }
}
