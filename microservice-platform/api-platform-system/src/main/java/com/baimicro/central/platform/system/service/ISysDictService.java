package com.baimicro.central.platform.system.service;

import com.baimicro.central.platform.pojo.entity.SysDict;
import com.baimicro.central.platform.pojo.entity.SysDictItem;
import com.baimicro.central.platform.pojo.model.platform.DictModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
public interface ISysDictService extends IService<SysDict> {
    public List<DictModel> queryDictItemsByCode(String code);

    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    public String queryDictTextByKey(String code, String key);

    String queryTableDictTextByKey(String table, String text, String code, String key);

    /**
     * 根据字典类型删除关联表中其对应的数据
     *
     * @param sysDict
     * @return
     */
    boolean deleteByDictId(SysDict sysDict);

    /**
     * 添加一对多
     */
    public void saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList);

    /**
     * 查询所有部门 作为字典信息 id -->value,departName -->text
     * @return
     */
    public List<DictModel> queryAllDepartBackDictModel();

    /**
     * 查询所有用户  作为字典信息 username -->value,realname -->text
     * @return
     */
    public List<DictModel> queryAllUserBackDictModel();
}
