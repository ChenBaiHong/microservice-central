package com.baimicro.central.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO mapper 父类，注意这个类不要让 mp 扫描到！！
 * version 0.1
 */
public interface SuperMapper<T> extends BaseMapper<T> {
    // 这里可以放一些公共的方法
}
