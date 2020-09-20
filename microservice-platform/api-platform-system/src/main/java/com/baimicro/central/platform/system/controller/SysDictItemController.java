package com.baimicro.central.platform.system.controller;

import com.alibaba.fastjson.JSON;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.platform.component.query.QueryGenerator;
import com.baimicro.central.platform.component.utils.ConvertUtils;
import com.baimicro.central.platform.pojo.entity.SysDictItem;
import com.baimicro.central.platform.system.service.ISysDictItemService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
@RequestMapping("/platform/dictItem")
public class SysDictItemController {
    @Autowired
    private ISysDictItemService sysDictItemService;

    /**
     * 分页列表查询
     *
     * @param sysDictItem
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<IPage<SysDictItem>> queryPageList(SysDictItem sysDictItem,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        Result<IPage<SysDictItem>> result = new Result<IPage<SysDictItem>>();
        QueryWrapper<SysDictItem> queryWrapper = QueryGenerator.initQueryWrapper(sysDictItem, req.getParameterMap());
        Page<SysDictItem> page = new Page<SysDictItem>(pageNo, pageSize);
        IPage<SysDictItem> pageList = sysDictItemService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param sysDictItem
     * @return
     */
    @PostMapping(value = "/add")
    public Result<SysDictItem> add(@RequestBody SysDictItem sysDictItem) {
        try {
            sysDictItemService.save(sysDictItem);
            return Result.succeed("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("操作失败");
        }
    }

    /**
     * 编辑
     *
     * @param sysDictItem
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<SysDictItem> edit(@RequestBody SysDictItem sysDictItem) {
        SysDictItem sysDictItemEntity = sysDictItemService.getById(sysDictItem.getId());
        if (sysDictItemEntity == null) {
            return Result.failed("未找到对应实体");
        } else {
            boolean ok = sysDictItemService.updateById(sysDictItem);
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
            sysDictItemService.removeById(id);
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
    public Result<SysDictItem> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        if (ids == null || "".equals(ids.trim())) {
            return Result.failed("参数不识别！");
        } else {
            this.sysDictItemService.removeByIds(Arrays.asList(ids.split(",")));
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
    public Result<SysDictItem> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysDictItem> result = new Result<SysDictItem>();
        SysDictItem sysDictItem = sysDictItemService.getById(id);
        if (sysDictItem == null) {
            return Result.failed("未找到对应实体");
        } else {
            result.setResult(sysDictItem);
            result.setSuccess(true);
        }
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
        QueryWrapper<SysDictItem> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (ConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                SysDictItem sysDictItem = JSON.parseObject(deString, SysDictItem.class);
                queryWrapper = QueryGenerator.initQueryWrapper(sysDictItem, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysDictItem> pageList = sysDictItemService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "列表");
        mv.addObject(NormalExcelConstants.CLASS, SysDictItem.class);
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
                List<SysDictItem> listSysDictItems = ExcelImportUtil.importExcel(file.getInputStream(), SysDictItem.class, params);
                sysDictItemService.saveBatch(listSysDictItems);
                return Result.succeed("文件导入成功！数据行数:" + listSysDictItems.size());
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
