package com.baimicro.central.platform.system.service.impl;

import com.baimicro.central.platform.pojo.entity.SysDictItem;
import com.baimicro.central.platform.system.mapper.SysDictItemMapper;
import com.baimicro.central.platform.system.service.ISysDictItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

    @Autowired
    private SysDictItemMapper sysDictItemMapper;

    @Override
    public List<SysDictItem> selectItemsByMainId(String mainId) {
        return sysDictItemMapper.selectItemsByMainId(mainId);
    }
}
