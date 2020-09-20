package com.baimicro.central.oauth.controller;

import com.baimicro.central.common.model.Result;
import com.baimicro.central.oauth.dto.ClientDto;
import com.baimicro.central.oauth.mapper.ClientMapper;
import com.baimicro.central.oauth.model.Client;
import com.baimicro.central.common.model.DuplicateCheck;
import com.baimicro.central.oauth.service.IClientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO 应用相关接口
 * version 0.1
 */
@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private IClientService clientService;
    @Autowired
    private ClientMapper clientMapper;

    @GetMapping(value = "/list")
    public Result<IPage<Client>> list(Client client,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {

        Result<IPage<Client>> result = new Result<>();
        Page<Client> page = new Page<>(pageNo, pageSize);
        String key = req.getParameter("clientName");
        QueryWrapper<Client> wrapper = null;
        if (key != null) {
            wrapper = new QueryWrapper<>();
            wrapper.like("client_name", "%" + key + "%");
        }
        IPage<Client> pageList = clientService.page(page, wrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 校验数据是否在系统中是否存在
     *
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Result<Object> doDuplicateCheck(DuplicateCheck duplicateCheck, HttpServletRequest request) {
        Long num = null;

        if (StringUtils.isNotBlank(duplicateCheck.getDataId())) {
            // [2].编辑页面校验
            num = clientMapper.duplicateCheckCountSql(duplicateCheck);
        } else {
            // [1].添加页面校验
            num = clientMapper.duplicateCheckCountSqlNoDataId(duplicateCheck);
        }

        if (num == null || num == 0) {
            // 该值可用
            return Result.succeed("该值可用！");
        } else {
            // 该值不可用
            return Result.failed("该值不可用，系统中已存在！");
        }
    }

    @GetMapping("/{id}")
    public Result<Client> get(@PathVariable Long id) {

        return Result.succeed(clientService.getById(id));
    }

    @GetMapping("/all")
    public Result<List<Client>> allClient() {

        return Result.succeed(clientService.list());
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */

    @DeleteMapping(value = "/delete")
    public Result delete(@RequestParam(name = "id") String id) {
        return clientService.delClient(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result deleteBatch(@RequestParam(name = "ids") String ids) {
        return clientService.delClient(Arrays.asList(ids.split(",")));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "保存或者修改应用")
    public Result saveOrUpdate(@RequestBody ClientDto clientDto) throws Exception {
        return clientService.saveClient(clientDto);
    }
}

