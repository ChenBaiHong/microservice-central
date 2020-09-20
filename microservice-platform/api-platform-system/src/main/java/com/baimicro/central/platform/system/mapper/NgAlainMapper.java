package com.baimicro.central.platform.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NgAlainMapper extends BaseMapper {
    public List<Map<String, String>> getDictByTable(@Param("table") String table, @Param("key") String key, @Param("value") String value);

}
