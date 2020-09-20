package com.baimicro.central.platform.component.utils;

import com.baimicro.central.platform.pojo.entity.PlatfPermissionDataRule;
import com.baimicro.central.platform.pojo.vo.UserCacheInfo;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author baihoo.chen
 * @Description TODO 数据权限查询规则容器工具类
 * @Date 2018/11/12
 **/
public class DataAutorUtils {

    public static final String MENU_DATA_AUTHOR_RULES = "MENU_DATA_AUTHOR_RULES";

    public static final String MENU_DATA_AUTHOR_RULE_SQL = "MENU_DATA_AUTHOR_RULE_SQL";

    public static final String SYS_USER_INFO = "SYS_USER_INFO";

    /**
     * 往链接请求里面，传入数据查询条件
     *
     * @param request
     * @param MENU_DATA_AUTHOR_RULES
     */
    public static synchronized void installDataSearchConditon(HttpServletRequest request, List<PlatfPermissionDataRule> dataRules) {
        @SuppressWarnings("unchecked")
        List<PlatfPermissionDataRule> list = (List<PlatfPermissionDataRule>) loadDataSearchConditon();// 1.先从request获取MENU_DATA_AUTHOR_RULES，如果存则获取到LIST
        if (list == null) {
            // 2.如果不存在，则new一个list
            list = new ArrayList<PlatfPermissionDataRule>();
        }
        for (PlatfPermissionDataRule tsDataRule : dataRules) {
            list.add(tsDataRule);
        }
        request.setAttribute(MENU_DATA_AUTHOR_RULES, list); // 3.往list里面增量存指
    }

    /**
     * 获取请求对应的数据权限规则
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static synchronized List<PlatfPermissionDataRule> loadDataSearchConditon() {
        return (List<PlatfPermissionDataRule>) SpringContextUtils.getHttpServletRequest().getAttribute(MENU_DATA_AUTHOR_RULES);

    }

    /**
     * 获取请求对应的数据权限SQL
     *
     * @param request
     * @return
     */
    public static synchronized String loadDataSearchConditonSQLString() {
        return (String) SpringContextUtils.getHttpServletRequest().getAttribute(MENU_DATA_AUTHOR_RULE_SQL);
    }

    /**
     * 往链接请求里面，传入数据查询条件
     *
     * @param request
     * @param MENU_DATA_AUTHOR_RULE_SQL
     */
    public static synchronized void installDataSearchConditon(HttpServletRequest request, String sql) {
        String ruleSql = (String) loadDataSearchConditonSQLString();
        if (!StringUtils.hasText(ruleSql)) {
            request.setAttribute(MENU_DATA_AUTHOR_RULE_SQL, sql);
        }
    }

    public static synchronized void installUserInfo(HttpServletRequest request, UserCacheInfo userinfo) {
        request.setAttribute(SYS_USER_INFO, userinfo);
    }

    public static synchronized UserCacheInfo loadUserInfo() {
        return (UserCacheInfo) SpringContextUtils.getHttpServletRequest().getAttribute(SYS_USER_INFO);

    }
}
