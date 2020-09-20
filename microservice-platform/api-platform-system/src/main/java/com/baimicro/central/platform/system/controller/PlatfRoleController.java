package com.baimicro.central.platform.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.platform.component.query.QueryGenerator;
import com.baimicro.central.platform.component.utils.ConvertUtils;
import com.baimicro.central.platform.pojo.constant.PlatformCacheConstant;
import com.baimicro.central.platform.pojo.constant.PlatformCommonConstant;
import com.baimicro.central.platform.pojo.entity.PlatfPermission;
import com.baimicro.central.platform.pojo.entity.PlatfPermissionDataRule;
import com.baimicro.central.platform.pojo.entity.PlatfRole;
import com.baimicro.central.platform.pojo.entity.PlatfRolePermission;
import com.baimicro.central.platform.pojo.model.platform.TreeModel;
import com.baimicro.central.platform.system.service.IPlatfPermissionDataRuleService;
import com.baimicro.central.platform.system.service.IPlatfPermissionService;
import com.baimicro.central.platform.system.service.IPlatfRolePermissionService;
import com.baimicro.central.platform.system.service.IPlatfRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/platform/role")
public class PlatfRoleController {
    @Autowired
    private IPlatfRoleService platfRoleService;

    @Autowired
    private IPlatfPermissionDataRuleService platfPermissionDataRuleService;

    @Autowired
    private IPlatfRolePermissionService platfRolePermissionService;

    @Autowired
    private IPlatfPermissionService platfPermissionService;

    /**
     * 分页列表查询
     *
     * @param role
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<PlatfRole>> queryPageList(PlatfRole role,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        Result<IPage<PlatfRole>> result = new Result<>();
        QueryWrapper<PlatfRole> queryWrapper = QueryGenerator.initQueryWrapper(role, req.getParameterMap());
        Page<PlatfRole> page = new Page<PlatfRole>(pageNo, pageSize);
        IPage<PlatfRole> pageList = platfRoleService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param role
     * @return
     */
    @PreAuthorize("hasPermission(#request,'role:add')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<PlatfRole> add(@RequestBody PlatfRole role, HttpServletRequest request) {
        Result<PlatfRole> result;
        try {
            role.setCreateTime(LocalDateTime.now());
            platfRoleService.save(role);
            result = Result.succeed("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("操作失败");

        }
        return result;
    }

    /**
     * 编辑
     *
     * @param role
     * @return
     */
    @PreAuthorize("hasPermission(#request,'role:edit')")
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public Result<PlatfRole> edit(@RequestBody PlatfRole role, HttpServletRequest request) {
        Result<PlatfRole> result = new Result<>();
        PlatfRole sysrole = platfRoleService.getById(role.getId());
        if (sysrole == null) {
            result = Result.failed("未找到对应实体");
        } else {
            role.setUpdateTime(LocalDateTime.now());
            boolean ok = platfRoleService.updateById(role);
            //TODO 返回false说明什么？
            if (ok) {
                result = Result.succeed("修改成功!");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @CacheEvict(value = PlatformCacheConstant.LOGIN_USER_RULES_CACHE, allEntries = true)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#request,'role:delete')")
    public Result<PlatfRole> delete(@RequestParam(name = "id") String id, HttpServletRequest request) {
        Result<PlatfRole> result = new Result<>();
        PlatfRole sysrole = platfRoleService.getById(id);
        if (sysrole == null) {
            result = Result.failed("未找到对应实体");
        } else {
            boolean ok = platfRoleService.removeById(id);
            if (ok) {
                result = Result.succeed("删除成功!");
            }
        }

        return result;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @CacheEvict(value = PlatformCacheConstant.LOGIN_USER_RULES_CACHE, allEntries = true)
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#request,'role:delete')")
    public Result<PlatfRole> deleteBatch(@RequestParam(name = "ids") String ids, HttpServletRequest request) {
        Result<PlatfRole> result;
        if (ids == null || "".equals(ids.trim())) {
            result = Result.failed("参数不识别！");
        } else {
            this.platfRoleService.removeByIds(Arrays.asList(ids.split(",")));
            result = Result.succeed("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Result<PlatfRole> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<PlatfRole> result = new Result<PlatfRole>();
        PlatfRole sysrole = platfRoleService.getById(id);
        if (sysrole == null) {
            result = Result.failed("未找到对应实体");
        } else {
            result.setResult(sysrole);
            result.setSuccess(true);
        }
        return result;
    }

    @RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public Result<List<PlatfRole>> queryall() {
        Result<List<PlatfRole>> result = new Result<>();
        List<PlatfRole> list = platfRoleService.list();
        if (list == null || list.size() <= 0) {
            result = Result.failed("未找到角色信息");
        } else {
            result.setResult(list);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 校验角色编码唯一
     */
    @RequestMapping(value = "/checkRoleCode", method = RequestMethod.GET)
    public Result<Boolean> checkUsername(String id, String roleCode) {
        Result<Boolean> result = new Result<>();
        result.setResult(true);//如果此参数为false则程序发生异常
        log.info("--验证角色编码是否唯一---id:" + id + "--roleCode:" + roleCode);
        try {
            PlatfRole role = null;
            if (ConvertUtils.isNotEmpty(id)) {
                role = platfRoleService.getById(id);
            }
            PlatfRole newRole = platfRoleService.getOne(new QueryWrapper<PlatfRole>().lambda().eq(PlatfRole::getRoleCode, roleCode));
            if (newRole != null) {
                //如果根据传入的roleCode查询到信息了，那么就需要做校验了。
                if (role == null) {
                    //role为空=>新增模式=>只要roleCode存在则返回false
                    result.setSuccess(false);
                    result.setMessage("角色编码已存在");
                    return result;
                } else if (!id.equals(newRole.getId())) {
                    //否则=>编辑模式=>判断两者ID是否一致-
                    result.setSuccess(false);
                    result.setMessage("角色编码已存在");
                    return result;
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setResult(false);
            result.setMessage(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
//    @RequestMapping(value = "/exportXls")
//    public ModelAndView exportXls(PlatfRole platfRole, HttpServletRequest request) {
//        // Step.1 组装查询条件
//        QueryWrapper<PlatfRole> queryWrapper = QueryGenerator.initQueryWrapper(platfRole, request.getParameterMap());
//        //Step.2 AutoPoi 导出Excel
//        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
//        List<PlatfRole> pageList = platfRoleService.list(queryWrapper);
//        //导出文件名称
//        mv.addObject(NormalExcelConstants.FILE_NAME, "角色列表");
//        mv.addObject(NormalExcelConstants.CLASS, PlatfRole.class);
//        LoginUser user = SecurityContextHolderUtil.getPrincipal();
//        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("角色列表数据", "导出人:" + user.getRealname(), "导出信息"));
//        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
//        return mv;
//    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
//    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
//        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
//            MultipartFile file = entity.getValue();// 获取上传文件对象
//            ImportParams params = new ImportParams();
//            params.setTitleRows(2);
//            params.setHeadRows(1);
//            params.setNeedSave(true);
//            try {
//                List<PlatfRole> listPlatfRoles = ExcelImportUtil.importExcel(file.getInputStream(), PlatfRole.class, params);
//                for (PlatfRole platfRoleExcel : listPlatfRoles) {
//                    platfRoleService.save(platfRoleExcel);
//                }
//                return Result.succeed("文件导入成功！数据行数：" + listPlatfRoles.size());
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//                return Result.failed("文件导入失败:" + e.getMessage());
//            } finally {
//                try {
//                    file.getInputStream().close();
//                } catch (IOException e) {
//                    log.error(e.getMessage(), e);
//                }
//            }
//        }
//        return Result.failed("文件导入失败！");
//    }

    /**
     * 查询数据规则数据
     */
    @GetMapping(value = "/datarule/{permissionId}/{roleId}")
    public Result<?> loadDatarule(@PathVariable("permissionId") Long permissionId, @PathVariable("roleId") Long roleId) {
        List<PlatfPermissionDataRule> list = platfPermissionDataRuleService.getPermRuleListByPermId(permissionId);
        if (list == null || list.size() == 0) {
            return Result.failed("未找到权限配置信息");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("datarule", list);
            LambdaQueryWrapper<PlatfRolePermission> query = new LambdaQueryWrapper<PlatfRolePermission>()
                    .eq(PlatfRolePermission::getPermissionId, permissionId)
                    .eq(PlatfRolePermission::getRoleId, roleId);
            PlatfRolePermission platfRolePermission = platfRolePermissionService.getOne(query);
            if (platfRolePermission == null) {
                //return Result.failed("未找到角色菜单配置信息");
            } else {
                String drChecked = platfRolePermission.getDataRuleIds();
                if (ConvertUtils.isNotEmpty(drChecked)) {
                    map.put("drChecked", drChecked.endsWith(",") ? drChecked.substring(0, drChecked.length() - 1) : drChecked);
                }
            }
            return Result.succeed(map);
            //TODO 以后按钮权限的查询也走这个请求 无非在map中多加两个key
        }
    }

    /**
     * 保存数据规则至角色菜单关联表
     */
    @PostMapping(value = "/datarule")
    public Result<?> saveDatarule(@RequestBody JSONObject jsonObject) {
        try {
            String permissionId = jsonObject.getString("permissionId");
            String roleId = jsonObject.getString("roleId");
            String dataRuleIds = jsonObject.getString("dataRuleIds");
            log.info("保存数据规则>>" + "菜单ID:" + permissionId + "角色ID:" + roleId + "数据权限ID:" + dataRuleIds);
            LambdaQueryWrapper<PlatfRolePermission> query = new LambdaQueryWrapper<PlatfRolePermission>()
                    .eq(PlatfRolePermission::getPermissionId, permissionId)
                    .eq(PlatfRolePermission::getRoleId, roleId);
            PlatfRolePermission platfRolePermission = platfRolePermissionService.getOne(query);
            if (platfRolePermission == null) {
                return Result.failed("请先保存角色菜单权限!");
            } else {
                platfRolePermission.setDataRuleIds(dataRuleIds);
                this.platfRolePermissionService.updateById(platfRolePermission);
            }
        } catch (Exception e) {
            log.error("PlatfRoleController.saveDatarule()发生异常：" + e.getMessage());
            return Result.failed("保存失败");
        }
        return Result.succeed("保存成功!");
    }


    /**
     * 用户角色授权功能，查询菜单权限树
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<Map<String, Object>> queryTreeList(HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        //全部权限ids
        List<String> ids = new ArrayList<>();
        try {
            LambdaQueryWrapper<PlatfPermission> query = new LambdaQueryWrapper<>();
            query.eq(PlatfPermission::getDelFlag, PlatformCommonConstant.DEL_FLAG_0);
            query.orderByAsc(PlatfPermission::getSortNo);
            List<PlatfPermission> list = platfPermissionService.list(query);
            for (PlatfPermission per : list) {
                ids.add(per.getId() + "");
            }
            List<TreeModel> treeList = new ArrayList<>();
            getTreeModelList(treeList, list, null);
            Map<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("treeList", treeList); //全部树节点数据
            resMap.put("ids", ids);//全部树ids
            result.setResult(resMap);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    private void getTreeModelList(List<TreeModel> treeList, List<PlatfPermission> metaList, TreeModel temp) {
        for (PlatfPermission permission : metaList) {
            Long tempPid = permission.getParentId();
            TreeModel tree = new TreeModel(permission.getId() + "", tempPid, permission.getName(), permission.getRuleFlag(), permission.isLeaf());
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
}
