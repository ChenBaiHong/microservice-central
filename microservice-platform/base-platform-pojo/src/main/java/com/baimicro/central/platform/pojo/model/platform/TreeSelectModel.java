package com.baimicro.central.platform.pojo.model.platform;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baigle.chen
 * @Description TODO 树形下拉框
 * @Date 2019-08-15
 **/
@Getter
@Setter
public class TreeSelectModel implements Serializable {


	private String key;

	private String title;

	private boolean isLeaf;

	private String icon;

	private String parentId;

	private String value;


	private List<TreeSelectModel> children;

}
