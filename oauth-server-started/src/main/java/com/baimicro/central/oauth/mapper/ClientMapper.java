package com.baimicro.central.oauth.mapper;

import com.baimicro.central.db.mapper.SuperMapper;
import com.baimicro.central.oauth.model.Client;
import com.baimicro.central.common.model.DuplicateCheck;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO
 * version 0.1
 */
@Mapper
public interface ClientMapper extends SuperMapper<Client> {

    List<Client> findList(Page<Client> page, @Param("params") Map<String, Object> params);

    @Select("select DISTINCT client_id , client_name FROM oauth_client_details")
    List<Client> findClient();

    @Select("SELECT COUNT(*) FROM ${tableName} WHERE ${fieldName} = #{fieldVal}")
    Long duplicateCheckCountSqlNoDataId(DuplicateCheck duplicateCheck);

    @Select("SELECT COUNT(*) FROM ${tableName} WHERE ${fieldName} = #{fieldVal} and id != #{dataId}")
    Long duplicateCheckCountSql(DuplicateCheck duplicateCheck);
}
