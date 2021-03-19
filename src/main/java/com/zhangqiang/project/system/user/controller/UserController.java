package com.zhangqiang.project.system.user.controller;

import com.alibaba.fastjson.JSON;
import com.zhangqiang.common.constant.UserConstants;
import com.zhangqiang.common.utils.StringUtils;
import com.zhangqiang.common.utils.poi.ExcelUtil;
import com.zhangqiang.common.utils.security.ShiroUtils;
import com.zhangqiang.framework.aspectj.lang.annotation.Log;
import com.zhangqiang.framework.aspectj.lang.enums.BusinessType;
import com.zhangqiang.framework.web.controller.BaseController;
import com.zhangqiang.framework.web.domain.AjaxResult;
import com.zhangqiang.framework.web.page.TableDataInfo;
import com.zhangqiang.project.monitor.online.service.IUserOnlineService;
import com.zhangqiang.project.system.dept.domain.Dept;
import com.zhangqiang.project.system.post.service.IPostService;
import com.zhangqiang.project.system.role.service.IRoleService;
import com.zhangqiang.project.system.user.domain.User;
import com.zhangqiang.project.system.user.domain.UserRole;
import com.zhangqiang.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * 用户信息
 *
 * @author zhangqiang
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {
    @Autowired
    private IPostService postService;
    private String prefix = "system/user";
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserOnlineService userOnlineService;

    /**
     * 新增用户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("roles", roleService.selectRoleAll());
        mmap.put("posts", postService.selectPostAll());
        return prefix + "/add";
    }

    /**
     * 新增保存用户
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated User user) {
        String tenantcode = ShiroUtils.getSysUser().getTenantCode();
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName(), tenantcode))) {
            return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
        } else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user, tenantcode))) {
            return error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
        } else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user, tenantcode))) {
            return error("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }
        return toAjax(userService.insertUser(user));
    }

    /**
     * 进入授权角色页
     */
    @GetMapping("/authRole/{userId}")
    public String authRole(@PathVariable("userId") Long userId, ModelMap mmap) {
        User user = userService.selectUserById(userId);
        // 获取用户所属的角色列表
        List<UserRole> userRoles = userService.selectUserRoleByUserId(userId);
        mmap.put("user", user);
        mmap.put("userRoles", userRoles);
        return prefix + "/authRole";
    }

    /**
     * 用户状态修改
     */
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:user:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(User user) {
        userService.checkUserAllowed(user);
        return toAjax(userService.changeStatus(user));
    }

    /**
     * 校验email邮箱
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(User user) {
        return userService.checkEmailUnique(user, ShiroUtils.getSysUser().getTenantCode());
    }

    /**
     * 校验用户名
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(User user) {
        return userService.checkLoginNameUnique(user.getLoginName(), ShiroUtils.getSysUser().getTenantCode());
    }

    /**
     * 校验手机号码
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(User user) {
        return userService.checkPhoneUnique(user, ShiroUtils.getSysUser().getTenantCode());
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", userService.selectUserById(userId));
        mmap.put("roles", roleService.selectRolesByUserId(userId));
        mmap.put("posts", postService.selectPostsByUserId(userId));
        return prefix + "/edit";
    }

    /**
     * 修改保存用户
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated User user) {
        userService.checkUserAllowed(user);
        String tenantcode = ShiroUtils.getSysUser().getTenantCode();
        if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user, tenantcode))) {
            return error("修改用户'" + user.getLoginName() + "'失败，手机号码已存在");
        } else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user, tenantcode))) {
            return error("修改用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }
        return toAjax(userService.updateUser(user));
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(User user, @RequestParam("ids") String ids) {
        List<User> list;
        if (StringUtils.isBlank(ids)) {
            list = userService.selectUserList(user);
        } else {
            list = new LinkedList<>();

            String[] idArray = StringUtils.split(ids, ",");
            for (String id : idArray) {
                list.add(userService.selectUserById(Long.parseLong(id)));
            }
        }

        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:user:import")
    @PostMapping("/importData")
    @ResponseBody
    public void importData(MultipartFile file, boolean updateSupport, HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuilder printContent = new StringBuilder();
        String[] fileNameInfo = StringUtils.split(file.getOriginalFilename(), ".");
        if (!"xlsx".equals(fileNameInfo[fileNameInfo.length - 1]) && !"xls".equals(fileNameInfo[fileNameInfo.length - 1])) {
            printContent.append(JSON.toJSONString(AjaxResult.error("请下载模板填写！")));
        } else {
            ExcelUtil<User> util = new ExcelUtil<>(User.class);
            List<User> userList = util.importExcel(file.getInputStream());
            String message;
            try {
                message = userService.importUser(userList, updateSupport);
                printContent.append(JSON.toJSONString(AjaxResult.success(message)));
            } catch (Exception e) {
                message = e.getMessage();
                printContent.append(JSON.toJSONString(AjaxResult.error(message)));
                e.printStackTrace();
            }
        }

        // 如果是IE的话就设置
        if (request.getHeader("user-agent").contains("MSIE 9")) {
            response.setContentType("text/html");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(printContent);
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(printContent);
        }
    }

    @RequiresPermissions("system:user:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);

        List<User> exampleUserList = new LinkedList<>();

        User exampleUser = new User();
        exampleUser.setLoginName("qq674967111（登陆的账号名称，登陆界面使用【必填】）");
        exampleUser.setUserName("张三（登陆的用户名称，登记使用【必填】）");
        exampleUser.setEmail("674967111@qq.com（登陆的用户邮箱）");
        exampleUser.setPhonenumber("13888888888（登陆的用户手机号码）");
        exampleUser.setSex("未知（男/女/未知【必填】）");
        exampleUser.setStatus("正常（正常/停用【必填】）");
//        exampleUser.setDeptName("前台（如果有多个部门的名称是相同的，最好留空，后续手动分配。）");
        exampleUser.setRemark("录入前记得删除本行");
        exampleUserList.add(exampleUser);
        return util.importTemplateExcel(exampleUserList, "用户数据");
    }

    /**
     * 用户授权角色
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PostMapping("/authRole/insertAuthRole")
    @ResponseBody
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    @RequiresPermissions("system:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(User user) {
        startPage();
        List<User> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @RequiresPermissions("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(userService.deleteUserByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/resetPwd";
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwdSave(User user) {
//        userService.checkUserAllowed(user);
        if (userService.resetUserPwd(user) > 0) {
            user = userService.selectUserById(user.getUserId());
            if (ShiroUtils.getUserId().longValue() == user.getUserId().longValue()) {
                setSysUser(user);
            }
            userOnlineService.forceLogout(user);
            return success();
        }
        return error();
    }

    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user() {
        return prefix + "/user";
    }
}