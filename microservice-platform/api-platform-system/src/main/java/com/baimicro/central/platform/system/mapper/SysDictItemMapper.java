package com.baimicro.central.platform.system.mapper;

import com.baimicro.central.platform.pojo.entity.SysDictItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date:   2020-04-08
 * @Version: V1.0
 */
@Mapper
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

    @Select("SELECT * FROM SYS_DICT_ITEM WHERE DICT_ID = #{mainId}")
    public List<SysDictItem> selectItemsByMainId(String mainId);
}
