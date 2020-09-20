package com.baimicro.central.platform.system.service;

import com.baimicro.central.platform.pojo.entity.SysDictItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
public interface ISysDictItemService extends IService<SysDictItem> {

    public List<SysDictItem> selectItemsByMainId(String mainId);
}
