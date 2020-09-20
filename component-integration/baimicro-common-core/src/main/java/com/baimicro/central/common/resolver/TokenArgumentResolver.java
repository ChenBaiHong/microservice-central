package com.baimicro.central.common.resolver;

import cn.hutool.core.util.StrUtil;
import com.baimicro.central.common.annotation.LoginUser;
import com.baimicro.central.common.constant.SecurityConstants;
import com.baimicro.central.common.feign.UserService;
import com.baimicro.central.common.model.AppRole;
import com.baimicro.central.common.model.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO Token 转化 SysUser
 * version 0.1
 */
@Slf4j
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {
    private UserService userService;

    public TokenArgumentResolver(UserService userService) {
        this.userService = userService;
    }

    /**
     * 入参筛选
     *
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginUser.class) && methodParameter.getParameterType().equals(AppUser.class);
    }

    /**
     * @param methodParameter       入参集合
     * @param modelAndViewContainer model 和 view
     * @param nativeWebRequest      web相关
     * @param webDataBinderFactory  入参解析
     * @return 包装对象
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) {
        LoginUser loginUser = methodParameter.getParameterAnnotation(LoginUser.class);
        boolean isFull = loginUser.isFull();
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String userId = request.getHeader(SecurityConstants.USER_ID_HEADER);
        String username = request.getHeader(SecurityConstants.USER_HEADER);
        String roles = request.getHeader(SecurityConstants.ROLE_HEADER);
        if (StrUtil.isBlank(username)) {
            log.warn("resolveArgument error username is empty");
            return null;
        }
        AppUser user;
        if (isFull) {
            user = userService.selectByUsername(username);
        } else {
            user = new AppUser();
            user.setId(Long.valueOf(userId));
            user.setUsername(username);
        }
        List<AppRole> sysRoleList = new ArrayList<>();
        Arrays.stream(roles.split(",")).forEach(role -> {
            AppRole sysRole = new AppRole();
            sysRole.setCode(role);
            sysRoleList.add(sysRole);
        });
        user.setRoles(sysRoleList);
        return user;
    }
}
