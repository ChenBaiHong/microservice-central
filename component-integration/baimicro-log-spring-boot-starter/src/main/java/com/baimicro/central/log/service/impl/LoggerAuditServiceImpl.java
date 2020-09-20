package com.baimicro.central.log.service.impl;

import com.baimicro.central.log.model.Audit;
import com.baimicro.central.log.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.time.format.DateTimeFormatter;

/**
 * @program: hospital-cloud-platform
 * @description: 审计日志实现类-打印日志
 * @author: baiHoo.chen
 * @create: 2020-04-07
 **/
@Slf4j
@ConditionalOnProperty(name = "his.cloud.audit-log.log-type", havingValue = "logger", matchIfMissing = true)
public class LoggerAuditServiceImpl implements IAuditService {
    private static final String MSG_PATTERN = "{}|{}|{}|{}|{}|{}|{}|{}";

    /**
     * 格式为：{时间}|{应用名}|{类名}|{方法名}|{用户id}|{用户名}|{租户id}|{操作信息}
     * 例子：2020-02-04 09:13:34.650|user-center|com.central.user.rpcservice.SysUserController|saveOrUpdate|1|admin|webApp|新增用户:admin
     */
    @Override
    public void save(Audit audit) {
        log.debug(MSG_PATTERN
                , audit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
                , audit.getApplicationName(), audit.getClassName(), audit.getMethodName()
                , audit.getUserId(), audit.getUserName(), audit.getClientId()
                , audit.getOperation());
    }
}
