package com.baimicro.central.oauth.controller;

import com.baimicro.central.common.model.Result;
import com.baimicro.central.oauth.mapper.ClientMapper;
import com.baimicro.central.oauth.model.TokenVo;
import com.baimicro.central.oauth.service.ITokensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO token管理接口
 * version 0.1
 */
@RestController
@RequestMapping("/tokens")
public class TokensController {

    @Autowired
    private ITokensService tokensService;

    @Autowired
    private ClientMapper clientMapper;

    @GetMapping("/list")
    public Result list(TokenVo tokenVo,
                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
            return tokensService.listTokens(tokenVo, pageNo, pageSize);
    }

    @GetMapping("/findClient")
    public Result findClient() {
        return Result.succeed(clientMapper.findClient());
    }
}

