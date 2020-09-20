package com.baimicro.central.platform.system.mapper;


import com.baimicro.central.common.model.DuplicateCheck;
import com.baimicro.central.platform.pojo.entity.SysDict;
import com.baimicro.central.platform.pojo.model.platform.DictModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
     *  重复检查SQL
     * @return
     */
    public Long duplicateCheckCountSql(DuplicateCheck duplicateCheck);
    public Long duplicateCheckCountSqlNoDataId(DuplicateCheck duplicateCheck);

    public List<DictModel> queryDictItemsByCode(@Param("code") String code);
    public List<DictModel> queryTableDictItemsByCode(@Param("table") String table, @Param("text") String text, @Param("code") String code);


    public String queryDictTextByKey(@Param("code") String code, @Param("key") String key);

    public String queryTableDictTextByKey(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("key") String key);


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
