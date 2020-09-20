package com.baimicro.central.platform.system.mapper;

import com.baimicro.central.platform.pojo.entity.PlatfPermission;
import com.baimicro.central.platform.pojo.model.platform.TreeModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;


/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Mapper
public interface PlatfPermissionMapper extends BaseMapper<PlatfPermission> {
    /**
     * 通过父菜单ID查询子菜单
     *
     * @param parentId
     * @return
     */
    public List<TreeModel> queryListByParentId(@Param("parentId") Long parentId);

    /**
     * 根据用户查询用户权限和菜单
     */
    public List<PlatfPermission> selectPermByUsername(@Param("username") String username);


    /**
     * 根据用户查询用户权限
     *
     * @param map
     * @return
     */
    public List<PlatfPermission> selectPerms(@Param("map") Map<String, Object> map);

    /**
     * 修改菜单状态字段： 是否子节点
     */
    @Update("update platf_permission set is_leaf=#{leaf} where id = #{id}")
    public int setMenuLeaf(@Param("id") Long id, @Param("leaf") int leaf);

    /**
     * 获取模糊匹配规则的数据权限URL
     */
    @Select("SELECT url FROM platf_permission WHERE del_flag = 0 and menu_type = 2 and url like '%*%'")
    public List<String> queryPermissionUrlWithStar();
}
