package com.baimicro.central.oauth.dto;

import com.baimicro.central.oauth.model.Client;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO
 * version 0.1
 */
@Setter
@Getter
public class ClientDto extends Client {

    private List<Long> permissionIds;

    private Set<Long> serviceIds;
}
