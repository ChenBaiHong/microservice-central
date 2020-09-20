package com.baimicro.central.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author baigle.chen
 * @Description TODO 重复校验VO
 * @Date 2019-08-15
 **/
@Data
public class DuplicateCheck implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 字段名
	 */
	private String fieldName;

	/**
	 * 字段值
	 */
	private String fieldVal;

	/**
	 * 数据ID
	*/
	private String dataId;

}
