package com.baimicro.central.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO
 * version 0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AppRole extends SuperEntity {

    private String code;
    private String name;
    private Long userId;
}
