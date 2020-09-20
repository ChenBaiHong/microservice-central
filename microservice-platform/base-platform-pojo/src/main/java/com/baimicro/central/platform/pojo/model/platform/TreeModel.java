package com.baimicro.central.platform.pojo.model.platform;

import com.baimicro.central.platform.pojo.entity.PlatfPermission;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author baigle.chen
 * @Description TODO 用户通告阅读标记表
 * @Date 2019-08-15
 **/
@Setter
@Getter
public class TreeModel implements Serializable {


	private String key;

	private String title;

	private String slotTitle;

	private boolean leaf;

	private String icon;

	private Integer ruleFlag;

	private Map<String,String> scopedSlots;

    private List<TreeModel> children;

    private Long parentId;

    private String label;

    private String value;

	public TreeModel() {

	}

	public TreeModel(PlatfPermission permission) {
		this.key = permission.getId()+"";
		this.icon = permission.getIcon();
		this.parentId = permission.getParentId();
		this.title = permission.getName();
		this.slotTitle =  permission.getName();
		this.value = permission.getId()+"";
		this.leaf = permission.isLeaf();
		this.label = permission.getName();
		if(!permission.isLeaf()) {
			this.children = new ArrayList<>();
		}
	}

	 public TreeModel( String key,Long parentId,String slotTitle,Integer ruleFlag,boolean leaf) {
    	this.key = key;
    	this.parentId = parentId;
    	this.ruleFlag=ruleFlag;
    	this.slotTitle =  slotTitle;
    	Map<String,String> map = new HashMap<>();
    	map.put("title", "hasDatarule");
    	this.scopedSlots = map;
    	this.leaf = leaf;
    	this.value = key;
    	if(!leaf) {
    		this.children = new ArrayList<>();
    	}
    }
}
