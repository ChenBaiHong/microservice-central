package com.baimicro.central.common.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 实体父类
 * version 0.1
 */
@Setter
@Getter
public class SuperEntity<T extends Model<?>> extends Model<T> {
    /**
     * 主键ID
     */
    private Long id;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
