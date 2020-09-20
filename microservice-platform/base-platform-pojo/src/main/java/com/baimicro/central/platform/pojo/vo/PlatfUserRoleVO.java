package com.baimicro.central.platform.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PlatfUserRoleVO implements Serializable{
	private static final long serialVersionUID = 1L;

	/**角色id*/
	private Long roleId;
	/**对应的用户id集合*/
	private List<Long> userIdList;

	public PlatfUserRoleVO(Long roleId, List<Long> userIdList) {
		super();
		this.roleId = roleId;
		this.userIdList = userIdList;
	}
}
