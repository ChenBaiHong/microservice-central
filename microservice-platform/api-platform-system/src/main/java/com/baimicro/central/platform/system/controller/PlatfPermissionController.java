package com.baimicro.central.platform.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.platform.component.utils.ConvertUtils;
import com.baimicro.central.platform.component.utils.MD5Util;
import com.baimicro.central.platform.component.utils.PermissionDataUtil;
import com.baimicro.central.platform.pojo.constant.PlatformCacheConstant;
import com.baimicro.central.platform.pojo.constant.PlatformCommonConstant;
import com.baimicro.central.platform.pojo.entity.PlatfPermission;
import com.baimicro.central.platform.pojo.entity.PlatfPermissionDataRule;
import com.baimicro.central.platform.pojo.entity.PlatfRolePermission;
import com.baimicro.central.platform.pojo.model.platform.PlatfPermissionTree;
import com.baimicro.central.platform.pojo.model.platform.TreeModel;
import com.baimicro.central.platform.system.service.IPlatfPermissionDataRuleService;
import com.baimicro.central.platform.system.service.IPlatfPermissionService;
import com.baimicro.central.platform.system.service.IPlatfRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/platform/permission")
public class PlatfPermissionController {

    @Autowired
    private IPlatfPermissionService platfPermissionService;

    @Autowired
    private IPlatfRolePermissionService platfRolePermissionService;

    @Autowired
    private IPlatfPermissionDataRuleService platfPermissionDataRuleService;

    /**
     * 加载数据节点
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<PlatfPermissionTree>> list() {
        Result<List<PlatfPermissionTree>> result = new Result<>();
        try {
            LambdaQueryWrapper<PlatfPermission> query = new LambdaQueryWrapper<PlatfPermission>();
            query.eq(PlatfPermission::getDelFlag, PlatformCommonConstant.DEL_FLAG_0);
            query.orderByAsc(PlatfPermission::getSortNo);
            List<PlatfPermission> list = platfPermissionService.list(query);
            List<PlatfPermissionTree> treeList = new ArrayList<>();
            getTreeList(treeList, list, null);
            result.setResult(treeList);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询用户拥有的菜单权限和按钮权限（根据TOKEN）
     *
     * @return
     */
    @RequestMapping(value = "/getUserPermission", method = RequestMethod.GET)
    public Result<?> getUserPermission(@RequestParam(name = "username") String username) {
        Result<JSONObject> result = new Result<JSONObject>();
        try {
            if (ConvertUtils.isEmpty(username)) {
                return Result.failed("未检测到您登陆，请重新登陆");
            }

            List<PlatfPermission> metaList = platfPermissionService.queryByUser(username);
            PermissionDataUtil.addIndexPage(metaList);
            JSONObject json = new JSONObject();

            // 菜单
            JSONArray menujsonArray = new JSONArray();
            this.getPermissionJsonArray(menujsonArray, metaList, null);

            // 权限
            JSONArray authjsonArray = new JSONArray();
            this.getAuthJsonArray(authjsonArray, metaList);

            //查询所有的权限
            LambdaQueryWrapper<PlatfPermission> query = new LambdaQueryWrapper<>();
            query.eq(PlatfPermission::getDelFlag, PlatformCommonConstant.DEL_FLAG_0);
            query.eq(PlatfPermission::getMenuType, PlatformCommonConstant.MENU_TYPE_2);

            List<PlatfPermission> allAuthList = platfPermissionService.list(query);
            // 所有权限
            JSONArray allauthjsonArray = new JSONArray();
            this.getAllAuthJsonArray(allauthjsonArray, allAuthList);
            json.put("menu", menujsonArray);
            json.put("auth", authjsonArray);
            json.put("allAuth", allauthjsonArray);
            result.setResult(json);
        } catch (Exception e) {
            result = Result.failed("查询失败:" + e.getMessage());
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 添加菜单
     *
     * @param permission
     * @return
     */
    // 指定角色权限，才具有操作此方法
    @PreAuthorize("hasAnyAuthority('admin')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<PlatfPermission> add(@RequestBody PlatfPermission permission) {
        Result<PlatfPermission> result;
        try {
            permission = PermissionDataUtil.intelligentProcessData(permission);
            platfPermissionService.addPermission(permission);
            result = Result.succeed("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("操作失败");
        }
        return result;
    }

    /**
     * 编辑菜单
     *
     * @param permission
     * @return
     */
    // 指定角色权限，才具有操作此方法
    @PreAuthorize("hasAnyAuthority('admin')")
    @CacheEvict(value = PlatformCacheConstant.LOGIN_USER_RULES_CACHE, allEntries = true)
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<PlatfPermission> edit(@RequestBody PlatfPermission permission) {
        Result<PlatfPermission> result;
        try {
            permission = PermissionDataUtil.intelligentProcessData(permission);
            platfPermissionService.editPermission(permission);
            result = Result.succeed("修改成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("操作失败");
        }
        return result;
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    // 指定角色权限，才具有操作此方法
    @PreAuthorize("hasAnyAuthority('admin')")
    @CacheEvict(value = PlatformCacheConstant.LOGIN_USER_RULES_CACHE, allEntries = true)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<PlatfPermission> delete(@RequestParam(name = "id") Long id) {
        Result<PlatfPermission> result;
        try {
            platfPermissionService.deletePermission(id);
            platfPermissionService.deletePermRuleByPermId(id);
            result = Result.succeed("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed(e.getMessage());
        }
        return result;
    }

    /**
     * 批量删除菜单
     *
     * @param ids
     * @return
     */
    // 指定角色权限，才具有操作此方法
    @PreAuthorize("hasAnyAuthority('admin')")
    @CacheEvict(value = PlatformCacheConstant.LOGIN_USER_RULES_CACHE, allEntries = true)
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<PlatfPermission> deleteBatch(@RequestParam(name = "ids") String ids) {
        Result<PlatfPermission> result = new Result<>();
        try {
            String[] arr = ids.split(",");
            for (String id : arr) {
                if (ConvertUtils.isNotEmpty(id)) {
                    platfPermissionService.deletePermission(Long.parseLong(id));
                }
            }
            result = Result.succeed("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("删除成功!");
        }
        return result;
    }

    /**
     * 获取全部的权限树
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<Map<String, Object>> queryTreeList() {
        Result<Map<String, Object>> result = new Result<>();
        // 全部权限ids
        List<Long> ids = new ArrayList<>();
        try {
            LambdaQueryWrapper<PlatfPermission> query = new LambdaQueryWrapper<>();
            query.eq(PlatfPermission::getDelFlag, PlatformCommonConstant.DEL_FLAG_0);
            query.orderByAsc(PlatfPermission::getSortNo);
            List<PlatfPermission> list = platfPermissionService.list(query);
            for (PlatfPermission permission : list) {
                ids.add(permission.getId());
            }
            List<TreeModel> treeList = new ArrayList<>();
            getTreeModelList(treeList, list, null);

            Map<String, Object> resMap = new HashMap<>();
            resMap.put("treeList", treeList); // 全部树节点数据
            resMap.put("ids", ids);// 全部树ids
            result.setResult(resMap);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 异步加载数据节点
     *
     * @return
     */
    @RequestMapping(value = "/queryListAsync", method = RequestMethod.GET)
    public Result<List<TreeModel>> queryAsync(@RequestParam(name = "pid", required = false) Long parentId) {
        Result<List<TreeModel>> result = new Result<>();
        try {
            List<TreeModel> list = platfPermissionService.queryListByParentId(parentId);
            if (list == null || list.size() <= 0) {
                result = Result.failed("未找到角色信息");
            } else {
                result.setResult(list);
                result.setSuccess(true);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return result;
    }

    /**
     * 查询角色授权
     *
     * @return
     */
    @RequestMapping(value = "/queryRolePermission", method = RequestMethod.GET)
    public Result<List<String>> queryRolePermission(@RequestParam(name = "roleId") Long roleId) {
        Result<List<String>> result = new Result<>();
        try {
            List<PlatfRolePermission> list = platfRolePermissionService.list(new QueryWrapper<PlatfRolePermission>().lambda().eq(PlatfRolePermission::getRoleId, roleId));
            result.setResult(list.stream().map(PlatfRolePermission -> String.valueOf(PlatfRolePermission.getPermissionId())).collect(Collectors.toList()));
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 保存角色授权
     *
     * @return
     */
    @RequestMapping(value = "/saveRolePermission", method = RequestMethod.POST)
    // 指定角色权限，才具有操作此方法
    @PreAuthorize("hasAnyAuthority('admin')")
    public Result<String> saveRolePermission(@RequestBody JSONObject json) {
        long start = System.currentTimeMillis();
        Result<String> result = new Result<>();
        try {
            String roleId = json.getString("roleId");
            String permissionIds = json.getString("permissionIds");
            String lastPermissionIds = json.getString("lastpermissionIds");
            this.platfRolePermissionService.saveRolePermission(Long.parseLong(roleId), permissionIds, lastPermissionIds);
            result = Result.succeed("保存成功！");
            log.info("======角色授权成功=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
        } catch (Exception e) {
            result = Result.failed("授权失败！");
            log.error(e.getMessage(), e);
        }
        return result;
    }

    private void getTreeList(List<PlatfPermissionTree> treeList, List<PlatfPermission> metaList, PlatfPermissionTree temp) {
        for (PlatfPermission permission : metaList) {
            Long tempPid = permission.getParentId();
            PlatfPermissionTree tree = new PlatfPermissionTree(permission);
            if (temp == null && tempPid == null) {
                treeList.add(tree);
                if (!tree.isLeaf()) {
                    getTreeList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
                temp.getChildren().add(tree);
                if (!tree.isLeaf()) {
                    getTreeList(treeList, metaList, tree);
                }
            }

        }
    }

    private void getTreeModelList(List<TreeModel> treeList, List<PlatfPermission> metaList, TreeModel temp) {
        for (PlatfPermission permission : metaList) {
            Long tempPid = permission.getParentId();
            TreeModel tree = new TreeModel(permission);
            if (temp == null && tempPid == null) {
                treeList.add(tree);
                if (!tree.isLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid == Long.parseLong(temp.getKey())) {
                temp.getChildren().add(tree);
                if (!tree.isLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            }

        }
    }

    /**
     * 获取权限JSON数组
     *
     * @param jsonArray
     * @param metaList
     * @param parentJson
     */
    private void getAllAuthJsonArray(JSONArray jsonArray, List<PlatfPermission> allList) {
        JSONObject json = null;
        for (PlatfPermission permission : allList) {
            json = new JSONObject();
            json.put("action", permission.getPerms());
            json.put("status", permission.getStatus());
            json.put("type", permission.getPermsType());
            json.put("describe", permission.getName());
            jsonArray.add(json);
        }
    }

    /**
     * 获取权限JSON数组
     *
     * @param jsonArray
     * @param metaList
     * @param parentJson
     */
    private void getAuthJsonArray(JSONArray jsonArray, List<PlatfPermission> metaList) {
        for (PlatfPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            JSONObject json = null;
            if (permission.getMenuType().equals(PlatformCommonConstant.MENU_TYPE_2) && PlatformCommonConstant.STATUS_1.equals(permission.getStatus())) {
                json = new JSONObject();
                json.put("action", permission.getPerms());
                json.put("type", permission.getPermsType());
                json.put("describe", permission.getName());
                jsonArray.add(json);
            }
        }
    }

    /**
     * 获取菜单JSON数组
     *
     * @param jsonArray
     * @param metaList
     * @param parentJson
     */
    private void getPermissionJsonArray(JSONArray jsonArray, List<PlatfPermission> metaList, JSONObject parentJson) {
        for (PlatfPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            Long tempPid = permission.getParentId();
            JSONObject json = getPermissionJsonObject(permission);
            if (json == null) {
                continue;
            }
            if (parentJson == null && ConvertUtils.isEmpty(tempPid)) {
                jsonArray.add(json);
                if (!permission.isLeaf()) {
                    getPermissionJsonArray(jsonArray, metaList, json);
                }
            } else if (parentJson != null && ConvertUtils.isNotEmpty(tempPid) && tempPid.equals(parentJson.getLong("id"))) {
                // 类型( 0：一级菜单 1：子菜单 2：按钮 )
                if (permission.getMenuType().equals(PlatformCommonConstant.MENU_TYPE_2)) {
                    JSONObject metaJson = parentJson.getJSONObject("meta");
                    if (metaJson.containsKey("permissionList")) {
                        metaJson.getJSONArray("permissionList").add(json);
                    } else {
                        JSONArray permissionList = new JSONArray();
                        permissionList.add(json);
                        metaJson.put("permissionList", permissionList);
                    }
                    // 类型( 0：一级菜单 1：子菜单 2：按钮 )
                } else if (permission.getMenuType().equals(PlatformCommonConstant.MENU_TYPE_1) || permission.getMenuType().equals(PlatformCommonConstant.MENU_TYPE_0)) {
                    if (parentJson.containsKey("children")) {
                        parentJson.getJSONArray("children").add(json);
                    } else {
                        JSONArray children = new JSONArray();
                        children.add(json);
                        parentJson.put("children", children);
                    }
                    if (!permission.isLeaf()) {
                        getPermissionJsonArray(jsonArray, metaList, json);
                    }
                }
            }

        }
    }

    private JSONObject getPermissionJsonObject(PlatfPermission permission) {
        JSONObject json = new JSONObject();
        // 类型(0：一级菜单 1：子菜单 2：按钮)
        if (permission.getMenuType().equals(PlatformCommonConstant.MENU_TYPE_2)) {
            //json.put("action", permission.getPerms());
            //json.put("type", permission.getPermsType());
            //json.put("describe", permission.getName());
            return null;
        } else if (permission.getMenuType().equals(PlatformCommonConstant.MENU_TYPE_0) || permission.getMenuType().equals(PlatformCommonConstant.MENU_TYPE_1)) {
            json.put("id", permission.getId());
            if (permission.isRoute()) {
                json.put("route", "1");// 表示生成路由
            } else {
                json.put("route", "0");// 表示不生成路由
            }

            if (isWWWHttpUrl(permission.getUrl())) {
                json.put("path", MD5Util.MD5Encode(permission.getUrl(), "utf-8"));
            } else {
                json.put("path", permission.getUrl());
            }

            // 重要规则：路由name (通过URL生成路由name,路由name供前端开发，页面跳转使用)
            if (ConvertUtils.isNotEmpty(permission.getComponentName())) {
                json.put("name", permission.getComponentName());
            } else {
                json.put("name", urlToRouteName(permission.getUrl()));
            }

            // 是否隐藏路由，默认都是显示的
            if (permission.isHidden()) {
                json.put("hidden", true);
            }
            // 聚合路由
            if (permission.isAlwaysShow()) {
                json.put("alwaysShow", true);
            }
            json.put("component", permission.getComponent());
            JSONObject meta = new JSONObject();
            // 由用户设置是否缓存页面 用布尔值
            if (permission.isKeepAlive()) {
                meta.put("keepAlive", true);
            } else {
                meta.put("keepAlive", false);
            }

            meta.put("title", permission.getName());
            if (ConvertUtils.isEmpty(permission.getParentId())) {
                // 一级菜单跳转地址
                json.put("redirect", permission.getRedirect());
                if (ConvertUtils.isNotEmpty(permission.getIcon())) {
                    meta.put("icon", permission.getIcon());
                }
            } else {
                if (ConvertUtils.isNotEmpty(permission.getIcon())) {
                    meta.put("icon", permission.getIcon());
                }
            }
            if (isWWWHttpUrl(permission.getUrl())) {
                meta.put("url", permission.getUrl());
            }
            if (permission.isInternalOrExternal()) {
                meta.put("internalOrExternal", true);
            }
            json.put("meta", meta);
        }

        return json;
    }

    /**
     * 判断是否外网URL 例如： http://localhost:8080/swagger-ui.html#/ 支持特殊格式： {{
     * window._CONFIG['domianURL'] }}/druid/ {{ JS代码片段 }}，前台解析会自动执行JS代码片段
     *
     * @return
     */
    private boolean isWWWHttpUrl(String url) {
        if (url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("{{"))) {
            return true;
        }
        return false;
    }

    /**
     * 通过URL生成路由name（去掉URL前缀斜杠，替换内容中的斜杠‘/’为-） 举例： URL = /isystem/role RouteName =
     * isystem-role
     *
     * @return
     */
    private String urlToRouteName(String url) {
        if (ConvertUtils.isNotEmpty(url)) {
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            url = url.replace("/", "-");

            // 特殊标记
            url = url.replace(":", "@");
            return url;
        } else {
            return null;
        }
    }

    /**
     * 根据菜单id来获取其对应的权限数据
     *
     * @param sysPermissionDataRule
     * @return
     */
    @RequestMapping(value = "/getPermRuleListByPermId", method = RequestMethod.GET)
    public Result<List<PlatfPermissionDataRule>> getPermRuleListByPermId(PlatfPermissionDataRule sysPermissionDataRule) {
        List<PlatfPermissionDataRule> permRuleList = platfPermissionDataRuleService.getPermRuleListByPermId(sysPermissionDataRule.getPermissionId());
        Result<List<PlatfPermissionDataRule>> result = new Result<>();
        result.setSuccess(true);
        result.setResult(permRuleList);
        return result;
    }

    /**
     * 添加菜单权限数据
     *
     * @param sysPermissionDataRule
     * @return
     */
    @RequestMapping(value = "/addPermissionRule", method = RequestMethod.POST)
    public Result<PlatfPermissionDataRule> addPermissionRule(@RequestBody PlatfPermissionDataRule sysPermissionDataRule) {
        Result<PlatfPermissionDataRule> result;
        try {
            sysPermissionDataRule.setCreateTime(LocalDateTime.now());
            platfPermissionDataRuleService.savePermissionDataRule(sysPermissionDataRule);
            result = Result.succeed("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("操作失败");
        }
        return result;
    }

    @RequestMapping(value = "/editPermissionRule", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<PlatfPermissionDataRule> editPermissionRule(@RequestBody PlatfPermissionDataRule sysPermissionDataRule) {
        Result<PlatfPermissionDataRule> result;
        try {
            platfPermissionDataRuleService.saveOrUpdate(sysPermissionDataRule);
            result = Result.succeed("更新成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("操作失败");
        }
        return result;
    }

    /**
     * 删除菜单权限数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deletePermissionRule", method = RequestMethod.DELETE)
    public Result<PlatfPermissionDataRule> deletePermissionRule(@RequestParam(name = "id", required = true) Long id) {
        Result<PlatfPermissionDataRule> result;
        try {
            platfPermissionDataRuleService.deletePermissionDataRule(id);
            result = Result.succeed("删除成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("操作失败");
        }
        return result;
    }

    /**
     * 查询菜单权限数据
     *
     * @param sysPermissionDataRule
     * @return
     */
    @RequestMapping(value = "/queryPermissionRule", method = RequestMethod.GET)
    public Result<List<PlatfPermissionDataRule>> queryPermissionRule(PlatfPermissionDataRule sysPermissionDataRule) {
        Result<List<PlatfPermissionDataRule>> result = new Result<>();
        try {
            List<PlatfPermissionDataRule> permRuleList = platfPermissionDataRuleService.queryPermissionRule(sysPermissionDataRule);
            result.setResult(permRuleList);
            result = Result.succeed("查询成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("操作失败");
        }
        return result;
    }
}
