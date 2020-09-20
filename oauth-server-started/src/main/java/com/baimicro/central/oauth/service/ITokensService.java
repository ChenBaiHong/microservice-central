package com.baimicro.central.oauth.service;

import com.baimicro.central.common.model.PageResult;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.oauth.model.TokenVo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO
 * version 0.1
 */
public interface ITokensService {

    /**
     * 查询token列表
     * @param tokenVo
     * @param pageNo
     * @param pageSize
     * @return
     */
    Result listTokens(TokenVo tokenVo, Integer pageNo, Integer pageSize);
}
