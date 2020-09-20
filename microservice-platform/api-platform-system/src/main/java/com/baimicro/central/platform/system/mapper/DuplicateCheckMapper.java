package com.baimicro.central.platform.system.mapper;


import com.baimicro.central.common.model.DuplicateCheck;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Mapper
public interface DuplicateCheckMapper {

    /**
     *  重复检查SQL
     * @return
     */
    public Long duplicateCheckCountSql(DuplicateCheck duplicateCheck);
    public Long duplicateCheckCountSqlNoDataId(DuplicateCheck duplicateCheck);
}
