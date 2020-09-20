package com.baimicro.central.platform.system.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baimicro.central.common.model.Result;
import com.baimicro.central.log.annotation.AuditLog;
import com.baimicro.central.platform.component.query.QueryGenerator;
import com.baimicro.central.platform.component.utils.ConvertUtils;
import com.baimicro.central.platform.pojo.entity.PlatfUser;
import com.baimicro.central.platform.pojo.entity.PlatfUserRole;
import com.baimicro.central.platform.pojo.vo.PlatfUserRoleVO;
import com.baimicro.central.platform.system.service.IPlatfUserRoleService;
import com.baimicro.central.platform.system.service.IPlatfUserService;
import com.baimicro.central.redis.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: baiHoo.chen
 * @Date: 2020-04-08
 * @Version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/platform/user")
public class PlatfUserController {

    @Autowired
    private IPlatfUserService platfUserService;

    @Autowired
    private IPlatfUserRoleService platfUserRoleService;

    @Autowired
    private IPlatfUserRoleService userRoleService;

    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<PlatfUser>> queryPageList(PlatfUser user,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        Result<IPage<PlatfUser>> result = new Result<>();
        QueryWrapper<PlatfUser> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
        Page<PlatfUser> page = new Page<>(pageNo, pageSize);
        IPage<PlatfUser> pageList = platfUserService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#request,'user:add')")
    public Result<PlatfUser> add(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        Result<PlatfUser> result;
        String selectedRoles = jsonObject.getString("selectedroles");
        try {
            PlatfUser user = JSON.parseObject(jsonObject.toJSONString(), PlatfUser.class);
            user.setCreateTime(LocalDateTime.now());//设置创建时间
            user.setPasswordEncoder(user.getPassword());
            platfUserService.addUserWithRole(user, selectedRoles);
            result = Result.succeed("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("操作失败");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#request,'user:edit')")
    @AuditLog(operation = "'更新用户:' + #jsonObject")
    public Result<PlatfUser> edit(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        Result<PlatfUser> result = new Result<>();
        try {
            PlatfUser platfUser = platfUserService.getById(jsonObject.getString("id"));
            if (platfUser == null) {
                result = Result.failed("未找到对应实体");
            } else {
                PlatfUser user = JSON.parseObject(jsonObject.toJSONString(), PlatfUser.class);
                user.setUpdateTime(LocalDateTime.now());
                user.setPassword(platfUser.getPassword());
                String roles = jsonObject.getString("selectedroles");
                platfUserService.editUserWithRole(user, roles);
                result = Result.succeed("修改成功!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("操作失败");
        }
        return result;
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#request,'user:delete')")
    @AuditLog(operation = "'删除用户:' + #id")
    public Result<PlatfUser> delete(@RequestParam(name = "id", required = true) String id, HttpServletRequest request) {
        if ("1".equals(id)) {
            return Result.failed("禁止删除 admin 账号!");
        }
        Result<PlatfUser> result = new Result<>();
        platfUserService.removeById(id);
        return result;
    }

    /**
     * 批量删除用户
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#request,'user:delete')")
    public Result<PlatfUser> deleteBatch(@RequestParam(name = "ids") String ids, HttpServletRequest request) {
        // 定义 PlatfUserDepart 实体类的数据库查询对象 LambdaQueryWrapper
        Result<PlatfUser> result;
        if (StrUtil.isEmpty(ids)) {
            result = Result.failed("参数不识别！");
        } else {
            List<String> list = Arrays.asList(ids.split(",")).stream().filter(e -> !"1".equals(e)).collect(Collectors.toList());
            this.platfUserService.removeByIds(list);
            result = Result.succeed("除 admin 用户，删除成功!");
        }
        return result;
    }

    /**
     * 冻结&解冻用户
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
    public Result<PlatfUser> frozenBatch(@RequestBody JSONObject jsonObject) {
        Result<PlatfUser> result;
        try {
            String ids = jsonObject.getString("ids");
            String status = jsonObject.getString("status");
            String[] arr = ids.split(",");
            for (String id : arr) {
                if (ConvertUtils.isNotEmpty(id)) {
                    this.platfUserService.update(new PlatfUser().setEnabled(Integer.parseInt(status)),
                            new UpdateWrapper<PlatfUser>().lambda().eq(PlatfUser::getId, id));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("操作失败" + e.getMessage());
        }
        result = Result.succeed("操作成功!");
        return result;

    }

    /**
     * 根据 ID 获取用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public Result<PlatfUser> getById(@RequestParam(name = "id") String id) {
        Result<PlatfUser> result = new Result<>();
        PlatfUser platfUser = platfUserService.getById(id);
        if (platfUser == null) {
            result = Result.failed("未找到对应实体");
        } else {
            result.setResult(platfUser);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 根据 username 获取用户信息
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/getByUsername", method = RequestMethod.GET)
    public Result<JSONObject> getByUsername(@RequestParam(name = "username") String username) {
        Result<JSONObject> result = new Result<>();
        PlatfUser platfUser = platfUserService.getUserByName(username);
        if (platfUser == null) {
            result = Result.failed("未找到对应实体");
            return result;
        }
        return userInfo(platfUser, result);
    }

    /**
     * 根据 phone 获取用户信息
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/getByPhone", method = RequestMethod.GET)
    public Result<JSONObject> getByPhone(@RequestParam(name = "phone") String phone) {
        Result<JSONObject> result = new Result<>();
        PlatfUser platfUser = platfUserService.getUserByPhone(phone);
        if (platfUser == null) {
            result = Result.failed("未找到对应实体");
            return result;
        }
        return userInfo(platfUser, result);
    }

    /**
     * 用户信息
     *
     * @param platfUser
     * @param result
     * @return
     */
    private Result<JSONObject> userInfo(PlatfUser platfUser, Result<JSONObject> result) {
        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        obj.put("userInfo", platfUser);
        result.setResult(obj);
        result.setMessage("登录成功");
        return result;
    }

    @RequestMapping(value = "/queryUserRole", method = RequestMethod.GET)
    public Result<List<Long>> queryUserRole(@RequestParam(name = "userid", required = true) Long userid) {
        Result<List<Long>> result = new Result<>();
        List<Long> list = new ArrayList<>();
        List<PlatfUserRole> userRole = platfUserRoleService.list(new QueryWrapper<PlatfUserRole>().lambda().eq(PlatfUserRole::getUserId, userid));
        if (userRole == null || userRole.size() <= 0) {
            result = Result.failed("未找到用户相关角色信息");
        } else {
            for (PlatfUserRole platfUserRole : userRole) {
                list.add(platfUserRole.getRoleId());
            }
            result.setSuccess(true);
            result.setResult(list);
        }
        return result;
    }


    /**
     * 校验用户账号是否唯一<br>
     * 可以校验其他 需要检验什么就传什么。。。
     *
     * @param platfUser
     * @return
     */
    @RequestMapping(value = "/checkOnlyUser", method = RequestMethod.GET)
    public Result<Boolean> checkOnlyUser(PlatfUser platfUser) {
        Result<Boolean> result = new Result<>();
        //如果此参数为false则程序发生异常
        result.setResult(true);
        try {
            //通过传入信息查询新的用户信息
            PlatfUser user = platfUserService.getOne(new QueryWrapper<PlatfUser>(platfUser));
            if (user != null) {
                result.setSuccess(false);
                result.setMessage("用户账号已存在");
                return result;
            }

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/changPassword", method = RequestMethod.PUT)
    public Result<PlatfUser> changPassword(@RequestBody PlatfUser platfUser) {
        Result<PlatfUser> result = new Result<PlatfUser>();
        String password = platfUser.getPassword();
        platfUser = this.platfUserService.getOne(new LambdaQueryWrapper<PlatfUser>().eq(PlatfUser::getUsername, platfUser.getUsername()));
        if (platfUser == null) {
            result = Result.failed("未找到对应实体");
        } else {
            platfUser.setPasswordEncoder(password);
            this.platfUserService.updateById(platfUser);
            result.setResult(platfUser);
        }
        return result;
    }

    /**
     * 查询所有用户所对应的角色信息
     *
     * @return
     */
    @RequestMapping(value = "/queryUserRoleMap", method = RequestMethod.GET)
    public Result<Map<Long, String>> queryUserRole() {
        Result<Map<Long, String>> result = new Result<>();
        Map<Long, String> map = userRoleService.queryUserRole();
        result.setResult(map);
        result.setSuccess(true);
        return result;
    }


    /**
     * @param userIds
     * @return
     * @功能：根据id 批量查询
     */
    @RequestMapping(value = "/queryByIds", method = RequestMethod.GET)
    public Result<Collection<PlatfUser>> queryByIds(@RequestParam String userIds) {
        Result<Collection<PlatfUser>> result = new Result<>();
        String[] userId = userIds.split(",");
        Collection<String> idList = Arrays.asList(userId);
        Collection<PlatfUser> userRole = platfUserService.listByIds(idList);
        result.setSuccess(true);
        result.setResult(userRole);
        return result;
    }

    /**
     * 首页密码修改
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public Result<PlatfUser> changPassword(@RequestBody JSONObject json) {
        Result<PlatfUser> result = new Result<PlatfUser>();
        String username = json.getString("username");
        String oldpassword = json.getString("oldpassword");
        PlatfUser user = this.platfUserService.getOne(new LambdaQueryWrapper<PlatfUser>().eq(PlatfUser::getUsername, username));
        if (user == null) {
            result = Result.failed("未找到用户!");
            return result;
        }
        if (!user.isPasswordMatches(oldpassword, user.getPassword())) {
            result = Result.failed("旧密码输入错误!");
            return result;
        }

        String password = json.getString("password");
        String confirmpassword = json.getString("confirmpassword");
        if (ConvertUtils.isEmpty(password)) {
            result = Result.failed("新密码不存在!");
            return result;
        }

        if (!password.equals(confirmpassword)) {
            result = Result.failed("两次输入密码不一致!");
            return result;
        }
        String newpassword = new BCryptPasswordEncoder().encode(password);
        this.platfUserService.update(new PlatfUser().setPassword(newpassword), new LambdaQueryWrapper<PlatfUser>().eq(PlatfUser::getId, user.getId()));
        result = Result.succeed("密码修改完成！");
        return result;
    }

    @RequestMapping(value = "/userRoleList", method = RequestMethod.GET)
    public Result<IPage<PlatfUser>> userRoleList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<PlatfUser>> result = new Result<IPage<PlatfUser>>();
        Page<PlatfUser> page = new Page<PlatfUser>(pageNo, pageSize);
        String roleId = req.getParameter("roleId");
        String username = req.getParameter("username");
        IPage<PlatfUser> pageList = platfUserService.getUserByRoleId(page, Long.parseLong(roleId), username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 给指定角色添加用户
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/addPlatfUserRole", method = RequestMethod.POST)
    public Result addPlatfUserRole(@RequestBody PlatfUserRoleVO platfUserRoleVO) {
        Result result = new Result();
        try {
            Long roleId = platfUserRoleVO.getRoleId();
            for (Long platfUserId : platfUserRoleVO.getUserIdList()) {
                PlatfUserRole platfUserRole = new PlatfUserRole(platfUserId, roleId);
                QueryWrapper<PlatfUserRole> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("role_id", roleId).eq("user_id", platfUserId);
                PlatfUserRole one = platfUserRoleService.getOne(queryWrapper);
                if (one == null) {
                    platfUserRoleService.save(platfUserRole);
                }

            }
            result.setMessage("添加成功!");
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("出错了: " + e.getMessage());
            return result;
        }
    }

    /**
     * 删除指定角色的用户关系
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteUserRole", method = RequestMethod.DELETE)
    public Result<PlatfUserRole> deleteUserRole(@RequestParam(name = "roleId") String roleId,
                                                @RequestParam(name = "userId", required = true) String userId
    ) {
        Result<PlatfUserRole> result;
        try {
            QueryWrapper<PlatfUserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_id", roleId).eq("user_id", userId);
            platfUserRoleService.remove(queryWrapper);
            result = Result.succeed("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("删除失败！");
        }
        return result;
    }

    /**
     * 批量删除指定角色的用户关系
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteUserRoleBatch", method = RequestMethod.DELETE)
    public Result<PlatfUserRole> deleteUserRoleBatch(
            @RequestParam(name = "roleId") String roleId,
            @RequestParam(name = "userIds", required = true) String userIds) {
        Result<PlatfUserRole> result;
        try {
            QueryWrapper<PlatfUserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_id", roleId).in("user_id", Arrays.asList(userIds.split(",")));
            platfUserRoleService.remove(queryWrapper);
            result = Result.succeed("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Result.failed("删除失败！");
        }
        return result;
    }

    /**
     * 部门用户列表
     */
    @RequestMapping(value = "/departUserList", method = RequestMethod.GET)
    public Result<IPage<PlatfUser>> departUserList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<PlatfUser>> result = new Result<>();
        Page<PlatfUser> page = new Page<>(pageNo, pageSize);
        String depId = req.getParameter("depId");
        String username = req.getParameter("username");
        IPage<PlatfUser> pageList = platfUserService.getUserByDepId(page, Long.parseLong(depId), username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    /**
     * 根据用户名或手机号查询用户信息
     *
     * @param platfUser
     * @return
     */
    @GetMapping("/queryPlatfUser")
    public Result<Map<String, Object>> queryPlatfUser(PlatfUser platfUser) {
        String phone = platfUser.getPhone();
        String username = platfUser.getUsername();
        Result<Map<String, Object>> result = new Result<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        if (ConvertUtils.isNotEmpty(phone)) {
            PlatfUser user = platfUserService.getUserByPhone(phone);
            if (user != null) {
                map.put("username", user.getUsername());
                map.put("phone", user.getPhone());
                result.setSuccess(true);
                result.setResult(map);
                return result;
            }
        }
        if (ConvertUtils.isNotEmpty(username)) {
            PlatfUser user = platfUserService.getUserByName(username);
            if (user != null) {
                map.put("username", user.getUsername());
                map.put("phone", user.getPhone());
                result.setSuccess(true);
                result.setResult(map);
                return result;
            }
        }
        result.setSuccess(false);
        result.setMessage("验证失败");
        return result;
    }

    /**
     * 用户手机号验证
     */
    @PostMapping("/phoneVerification")
    public Result<String> phoneVerification(@RequestBody JSONObject jsonObject) {
        Result<String> result = new Result<String>();
        String phone = jsonObject.getString("phone");
        String smscode = jsonObject.getString("smscode");
        Object code = redisUtil.get(phone);
        if (!smscode.equals(code)) {
            result.setMessage("手机验证码错误");
            result.setSuccess(false);
            return result;
        }
        redisUtil.set(phone, smscode);
        result.setResult(smscode);
        result.setSuccess(true);
        return result;
    }

    /**
     * 用户更改密码
     */
    @GetMapping("/passwordChange")
    public Result<PlatfUser> passwordChange(@RequestParam(name = "username") String username,
                                            @RequestParam(name = "password") String password,
                                            @RequestParam(name = "smscode") String smscode,
                                            @RequestParam(name = "phone") String phone) {
        Result<PlatfUser> result = new Result<PlatfUser>();
        PlatfUser platfUser;
        Object object = redisUtil.get(phone);
        if (null == object) {
            result.setMessage("更改密码失败");
            result.setSuccess(false);
        }
        if (!smscode.equals(object)) {
            result.setMessage("更改密码失败");
            result.setSuccess(false);
        }
        platfUser = this.platfUserService.getOne(new LambdaQueryWrapper<PlatfUser>().eq(PlatfUser::getUsername, username));
        if (platfUser == null) {
            result.setMessage("未找到对应实体");
            result.setSuccess(false);
            return result;
        } else {
            platfUser.setPasswordEncoder(password);
            this.platfUserService.updateById(platfUser);
            result.setSuccess(true);
            result.setMessage("密码修改完成！");
            return result;
        }
    }


}
