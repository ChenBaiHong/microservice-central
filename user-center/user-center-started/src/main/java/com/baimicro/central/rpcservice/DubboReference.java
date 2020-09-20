package com.baimicro.central.rpcservice;

import com.baimicro.central.common.constant.CacheConstant;
import com.baimicro.central.common.enums.ExcEnum;
import com.baimicro.central.common.exception.BusinessException;
import com.baimicro.central.common.model.OauthDubboServer;
import com.baimicro.central.redis.util.RedisUtil;
import com.baimicro.central.user.IAppAuthService;
import com.baimicro.central.user.IAppPermService;
import com.baimicro.central.user.IAppUserService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("all")
public class DubboReference {


    @Resource
    private RedisUtil redisUtil;

    /**
     * 根据 客户端ID获取对应用户服务
     *
     * @param clientId
     * @return
     */
    public IAppUserService getAppUserService(String clientId) {
        IAppUserService appService = getAppService(clientId, IAppUserService.class);
        if (appService != null)
            return appService;
        throw new BusinessException(ExcEnum.USER_SERVICE_NOT_EXIST);
    }

    /**
     * 根据 客户端ID获取对应权限角色服务
     *
     * @param clientId
     * @return
     */
    public IAppAuthService getAppAuthService(String clientId) {
        IAppAuthService appService = getAppService(clientId, IAppAuthService.class);
        if (appService != null)
            return appService;
        throw new BusinessException(ExcEnum.AUTH_SERVICE_NOT_EXIST);

    }

    /**
     * 根据 客户端ID获取对应权限服务
     *
     * @param clientId
     * @return
     */

    public IAppPermService getAppPermService(String clientId) {
        IAppPermService appService = getAppService(clientId,  IAppPermService.class);
        if (appService != null)
            return appService;
        throw new BusinessException(ExcEnum.PERM_SERVICE_NOT_EXIST);

    }

    private <T> T getAppService(String clientId,Class<T> tClass) {
        Object object = redisUtil.get(CacheConstant.CACHE_REGISTRY_DUBBO_SERVER);
        if (object != null) {
            List<OauthDubboServer> list = ((List<OauthDubboServer>) object).stream().filter(c -> clientId.equals(c.getClientId())
                    && tClass.getName().equals(c.getServiceInterface())).collect(Collectors.toList());
            if (list.size() >= 1) {
                ReferenceConfig<T> reference = new ReferenceConfig<>();
                reference.setApplication(new ApplicationConfig( list.get(0).getServerName()+ "-dubbo-consumer"));
                reference.setId(list.get(0).getServerName());
                reference.setRegistry(new RegistryConfig(list.get(0).getRegistryCenterUrl()));
                reference.setInterface(tClass);
                return reference.get();
            }
        }
        return null;
    }

}
