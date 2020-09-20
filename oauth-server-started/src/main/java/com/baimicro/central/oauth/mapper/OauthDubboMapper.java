package com.baimicro.central.oauth.mapper;

import com.baimicro.central.common.model.OauthDubboServer;
import com.baimicro.central.db.mapper.SuperMapper;
import com.baimicro.central.oauth.model.OauthServer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/12
 * @Description: TODO
 * version 0.1
 */
@Mapper
public interface OauthDubboMapper extends SuperMapper<OauthServer>  {

    @Select("SELECT id, client_id , service_interface , server_name , registry_center_url FROM oauth_dubbo_server")
    List<OauthDubboServer> findRegistryDubboServer();

}
