package com.baimicro.central.common.filter;

import cn.hutool.core.util.StrUtil;
import com.baimicro.central.common.constant.CommonConstant;
import com.baimicro.central.common.constant.SecurityConstants;
import com.baimicro.central.common.context.TenantContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/14
 * @Description: TODO 租户过滤器
 * version 0.1
 */
@ConditionalOnClass(Filter.class)
public class TenantFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            //优先获取请求参数中的tenantId值
            String tenantId = request.getParameter(CommonConstant.TENANT_ID_PARAM);
            if (StrUtil.isEmpty(tenantId)) {
                tenantId = request.getHeader(SecurityConstants.TENANT_HEADER);
            }
            //保存租户id
            if (StrUtil.isNotEmpty(tenantId)) {
                TenantContextHolder.setTenant(tenantId);
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }
}
