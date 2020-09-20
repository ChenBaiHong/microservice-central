package com.baimicro.central.platform.system.service.impl;

import com.baimicro.central.platform.pojo.constant.PlatformCacheConstant;
import com.baimicro.central.platform.pojo.entity.SysDict;
import com.baimicro.central.platform.pojo.entity.SysDictItem;
import com.baimicro.central.platform.pojo.model.platform.DictModel;
import com.baimicro.central.platform.system.mapper.SysDictItemMapper;
import com.baimicro.central.platform.system.mapper.SysDictMapper;
import com.baimicro.central.platform.system.service.ISysDictService;
import com.baimicro.central.redis.manager.CacheExpire;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Slf4j
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private SysDictItemMapper sysDictItemMapper;

    /**
     * 通过查询指定code 获取字典
     * @param code
     * @return
     */
    @Override
    @Cacheable(key = "'queryDictItemsByCode('+#code+')'"
            , value = PlatformCacheConstant.SYS_DICT_SERVICE
            , unless = "#result eq null or #result.size() eq 0")
    @CacheExpire(60 * 60 * 24) // 缓存两小时
    public List<DictModel> queryDictItemsByCode(String code) {
        log.info("无缓存dictCache的时候调用这里！");
        return sysDictMapper.queryDictItemsByCode(code);
    }

    /**
     * 通过查询指定code 获取字典值text
     * @param code
     * @param key
     * @return
     */

    @Override
    @Cacheable(key = "'queryDictTextByKey('+#code+','+#key+')'"
            , value = PlatformCacheConstant.SYS_DICT_SERVICE
            , unless = "#result eq null or #result.length() eq 0")
    @CacheExpire(60 * 60 * 24) // 缓存两小时
    public String queryDictTextByKey(String code, String key) {
        log.info("无缓存dictText的时候调用这里！");
        return sysDictMapper.queryDictTextByKey(code, key);
    }

    /**
     * 通过查询指定table的 text code 获取字典
     * dictTableCache采用redis缓存有效期10分钟
     * @param table
     * @param text
     * @param code
     * @return
     */
    @Override
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
        log.info("无缓存dictTableList的时候调用这里！");
        return sysDictMapper.queryTableDictItemsByCode(table,text,code);
    }

    /**
     * 通过查询指定table的 text code 获取字典值text
     * dictTableCache采用redis缓存有效期10分钟
     * @param table
     * @param text
     * @param code
     * @param key
     * @return
     */
    @Cacheable(key = "'queryTableDictTextByKey('+#table+','+#text+','+#code+','+#key+')'"
            , value = PlatformCacheConstant.SYS_DICT_SERVICE
            , unless = "#result eq null or #result.length() eq 0")
    @CacheExpire(60 * 60 * 24) // 缓存两小时
    public String queryTableDictTextByKey(String table,String text,String code, String key) {
        log.info("无缓存dictTable的时候调用这里！");
        return sysDictMapper.queryTableDictTextByKey(table,text,code,key);
    }

    /**
     * 根据字典类型id删除关联表中其对应的数据
     */
    @Override
    public boolean deleteByDictId(SysDict sysDict) {
        sysDict.setDelFlag(2);
        return  this.updateById(sysDict);
    }

    @Override
    @Transactional
    public void saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList) {

        sysDictMapper.insert(sysDict);
        if (sysDictItemList != null) {
            for (SysDictItem entity : sysDictItemList) {
                entity.setDictId(sysDict.getId());
                sysDictItemMapper.insert(entity);
            }
        }
    }

    @Override
    public List<DictModel> queryAllDepartBackDictModel() {
        return baseMapper.queryAllDepartBackDictModel();
    }

    @Override
    public List<DictModel> queryAllUserBackDictModel() {
        return baseMapper.queryAllUserBackDictModel();
    }

}
