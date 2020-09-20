package com.baimicro.central.platform.system.controller;


import com.baimicro.central.common.model.DuplicateCheck;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.platform.system.mapper.DuplicateCheckMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author baigle.chen
 * @Description TODO 重复校验工具
 * @Date 2019-08-15
 **/
@Slf4j
@RestController
@RequestMapping("/platform/duplicate")
public class DuplicateCheckController {

	@Autowired
    DuplicateCheckMapper duplicateCheckMapper;

	/**
	 * 校验数据是否在系统中是否存在
	 *
	 * @return
	 */
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public Result<Object> doDuplicateCheck(DuplicateCheck duplicateCheck, HttpServletRequest request) {
		Long num = null;

		log.info("----duplicate check------："+ duplicateCheck.toString());
		if (StringUtils.isNotBlank(duplicateCheck.getDataId())) {
			// [2].编辑页面校验
			num = duplicateCheckMapper.duplicateCheckCountSql(duplicateCheck);
		} else {
			// [1].添加页面校验
			num = duplicateCheckMapper.duplicateCheckCountSqlNoDataId(duplicateCheck);
		}

		if (num == null || num == 0) {
			// 该值可用
			return Result.succeed("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.failed("该值不可用，系统中已存在！");
		}
	}
}
