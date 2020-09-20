package com.baimicro.central.platform.system.controller;

import com.alibaba.fastjson.JSON;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.platform.component.query.QueryGenerator;
import com.baimicro.central.platform.component.utils.ConvertUtils;
import com.baimicro.central.platform.pojo.constant.PlatformCacheConstant;
import com.baimicro.central.platform.pojo.entity.SysDict;
import com.baimicro.central.platform.pojo.model.platform.DictModel;
import com.baimicro.central.platform.pojo.model.platform.SysDictTree;
import com.baimicro.central.platform.system.service.ISysDictService;
import com.baimicro.central.redis.manager.CacheExpire;
import com.baimicro.central.redis.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/platform/dict")
public class SysDictController {

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private RedisUtil redisUtil;
    /**
     * 分页列表查询
     *
     * @param sysDict
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
//    @Cacheable(key = "'queryPageList('+#pageNo+','+#pageNo+')'"
//            , value = PlatformCacheConstant.SYS_DICT_SERVICE)
//    @CacheExpire(60 * 60 * 2) // 缓存两小时
    @GetMapping(value = "/list")
    public Result<IPage<SysDict>> queryPageList(SysDict sysDict,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                HttpServletRequest req) {
        Result<IPage<SysDict>> result = new Result<IPage<SysDict>>();
        QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, req.getParameterMap());
        Page<SysDict> page = new Page<SysDict>(pageNo, pageSize);
        IPage<SysDict> pageList = sysDictService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * @param sysDict
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     * @功能：获取树形字典数据
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    public Result<List<SysDictTree>> treeList(SysDict sysDict, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<List<SysDictTree>> result = new Result<>();
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        // 构造查询条件
        String dictName = sysDict.getDictName();
        if (ConvertUtils.isNotEmpty(dictName)) {
            query.like(true, SysDict::getDictName, dictName);
        }
        query.eq(true, SysDict::getDelFlag, "1");
        query.orderByDesc(true, SysDict::getCreateTime);
        List<SysDict> list = sysDictService.list(query);
        List<SysDictTree> treeList = new ArrayList<>();
        for (SysDict node : list) {
            treeList.add(new SysDictTree(node));
        }
        result.setSuccess(true);
        result.setResult(treeList);
        return result;
    }

    /**
     * 获取字典数据
     *
     * @param dictCode 字典code
     * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
     * @return
     */
    @RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
    public Result<List<DictModel>> getDictItems(@PathVariable String dictCode) {
        log.info(" dictCode : " + dictCode);
        Result<List<DictModel>> result = new Result<List<DictModel>>();
        List<DictModel> ls = null;
        try {
            if (dictCode.contains(",")) {
                //关联表字典（举例：sys_user,realname,id）
                String[] params = dictCode.split(",");
                if (params.length != 3) {
                    return Result.failed("字典Code格式不正确！");
                }
                ls = sysDictService.queryTableDictItemsByCode(params[0], params[1], params[2]);
            } else {
                //字典表
                ls = sysDictService.queryDictItemsByCode(dictCode);
            }

            result.setSuccess(true);
            result.setResult(ls);
            log.info(result.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("操作失败");
        }

        return result;
    }

    /**
     * 获取字典数据
     *
     * @param dictCode
     * @return
     */
    @RequestMapping(value = "/getDictText/{dictCode}/{key}", method = RequestMethod.GET)
    public Result<String> getDictItems(@PathVariable("dictCode") String dictCode, @PathVariable("key") String key) {
        log.info(" dictCode : " + dictCode);
        Result<String> result = new Result<String>();
        String text = null;
        try {
            text = sysDictService.queryDictTextByKey(dictCode, key);
            result.setSuccess(true);
            result.setResult(text);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("操作失败");
        }
        return result;
    }

    /**
     * 添加
     *
     * @param sysDict
     * @return
     */
    @PostMapping(value = "/add")
    public Result<SysDict> add(@RequestBody SysDict sysDict) {
        try {
            sysDictService.save(sysDict);
            return Result.succeed("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("操作失败");
        }
    }

    /**
     * 编辑
     *
     * @param sysDict
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<SysDict> edit(@RequestBody SysDict sysDict) {
        SysDict sysDictEntity = sysDictService.getById(sysDict.getId());
        if (sysDictEntity == null) {
            return Result.failed("未找到对应实体");
        } else {
            boolean ok = sysDictService.updateById(sysDict);
            if (ok) return Result.succeed("修改成功!");
            else return Result.failed("修改失败!");
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            sysDictService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.failed("删除失败!");
        }
        return Result.succeed("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysDict> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        if (ids == null || "".equals(ids.trim())) {
            return Result.failed("参数不识别！");
        } else {
            this.sysDictService.removeByIds(Arrays.asList(ids.split(",")));
            return Result.succeed("删除成功!");
        }
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<SysDict> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysDict> result = new Result<SysDict>();
        SysDict sysDict = sysDictService.getById(id);
        if (sysDict == null) {
            return Result.failed("未找到对应实体");
        } else {
            result.setResult(sysDict);
            result.setSuccess(true);
        }
        return result;
    }
    /**
     * @功能：刷新缓存
     * @return
     */
    @RequestMapping(value = "/refreshCache")
    public Result<?> refreshCache() {
        Result<?> result = new Result<SysDict>();
        //清空字典缓存
        redisUtil.delAll(PlatformCacheConstant.SYS_DICT_SERVICE+"*");
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
        // Step.1 组装查询条件
        QueryWrapper<SysDict> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (ConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                SysDict sysDict = JSON.parseObject(deString, SysDict.class);
                queryWrapper = QueryGenerator.initQueryWrapper(sysDict, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysDict> pageList = sysDictService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "列表");
        mv.addObject(NormalExcelConstants.CLASS, SysDict.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("列表数据", "导出人:Jeecg", "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SysDict> listSysDicts = ExcelImportUtil.importExcel(file.getInputStream(), SysDict.class, params);
                sysDictService.saveBatch(listSysDicts);
                return Result.succeed("文件导入成功！数据行数:" + listSysDicts.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.failed("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.succeed("文件导入失败！");
    }

}
