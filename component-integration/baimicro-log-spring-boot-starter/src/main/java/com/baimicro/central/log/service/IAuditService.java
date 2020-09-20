package com.baimicro.central.log.service;

import com.baimicro.central.log.model.Audit;

/**
 * @program: hospital-cloud-platform
 * @description: 审计日志接口
 * @author: baiHoo.chen
 * @create: 2020-04-07
 **/
public interface IAuditService {
    void save(Audit audit);
}
