package com.baimicro.central.oauth.controller;

import cn.hutool.core.util.RandomUtil;
import com.baimicro.central.common.constant.CommonConstant;
import com.baimicro.central.common.constant.SecurityConstants;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.common.utils.RandImageUtil;
import com.baimicro.central.oauth.service.IValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16 23:44
 * @Description: TODO 验证码提供
 * version 0.1
 */
@Controller
public class ValidateCodeController {
    @Autowired
    private IValidateCodeService validateCodeService;

    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    /**
     * 创建验证码
     *
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{deviceId}")
    public Result createCode(@PathVariable String deviceId, HttpServletResponse response) throws Exception {
        Assert.notNull(deviceId, "机器码不能为空");
        Result<String> res = new Result<String>();
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES, 4);
            String lowerCaseCode = code.toLowerCase();
            validateCodeService.saveImageCode(deviceId, lowerCaseCode);
            String base64 = RandImageUtil.generate(code);
            res.setSuccess(true);
            res.setResult(base64);
        } catch (Exception e) {
            res.setMessage("获取验证码出错" + e.getMessage());
            res.setCode(CommonConstant.SC_ERR);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 发送手机验证码
     * 后期要加接口限制
     *
     * @param mobile 手机号
     * @return R
     */
    @ResponseBody
    @GetMapping(SecurityConstants.MOBILE_VALIDATE_CODE_URL_PREFIX + "/{mobile}")
    public Result createCode(@PathVariable String mobile, HttpServletRequest request) {
        Assert.notNull(mobile, "手机号不能为空");
        return validateCodeService.sendSmsCode(mobile);
    }
}

