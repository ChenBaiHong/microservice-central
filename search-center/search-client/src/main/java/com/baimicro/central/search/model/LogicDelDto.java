package com.baimicro.central.search.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/17
 * @Description: TODO 逻辑删除条件对象
 * version 0.1
 */
@Setter
@Getter
@AllArgsConstructor
public class LogicDelDto {
    /**
     * 逻辑删除字段名
     */
    private String logicDelField;
    /**
     * 逻辑删除字段未删除的值
     */
    private String logicNotDelValue;
}
