package com.zhangqiang.project.tool.swagger.controller;

import com.zhangqiang.common.constant.ShiroConstants;
import com.zhangqiang.common.constant.UserConstants;
import com.zhangqiang.common.utils.ServletUtils;
import com.zhangqiang.common.utils.StringUtils;
import com.zhangqiang.common.utils.file.FileUploadUtils;
import com.zhangqiang.common.utils.security.ShiroUtils;
import com.zhangqiang.framework.config.ZhangqiangCloudConfig;
import com.zhangqiang.framework.shiro.service.PasswordService;
import com.zhangqiang.framework.shiro.service.RegisterService;
import com.zhangqiang.framework.web.controller.BaseController;
import com.zhangqiang.framework.web.domain.AjaxResult;
import com.zhangqiang.project.system.user.domain.User;
import com.zhangqiang.project.system.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * swagger 用户公共对外接口
 *
 * @author zhangqiang
 */
@Api("用户公共接口")
@RestController
@RequestMapping("/commonuser")
public class CommonUserController extends BaseController
{

    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private RegisterService registerService;


    @ApiOperation(value = "用户登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登陆账号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "portalCode", value = "租户标识", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/login")
    public AjaxResult login(String username, String password, String portalCode) {
        if (StringUtils.isEmpty(portalCode))return error("租户标识不能为空");
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, false);
        Subject subject = SecurityUtils.getSubject();
        ServletUtils.getRequest().getSession().setAttribute(ShiroConstants.TENANT_CODE, portalCode);
        try
        {
            token.setRememberMe(false);
            subject.login(token);
            return success(subject.getSession().getId().toString());
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @ApiOperation(value = "用户信息")
    @GetMapping("/user")
    public AjaxResult thisuser() {
        User user = ShiroUtils.getSysUser();
        return AjaxResult.success(user);
    }

    @ApiOperation(value = "用户信息修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phonenumber", value = "手机号码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sex", value = "性别（0男 1女 2未知）", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping("/update")
    public AjaxResult update(String username, String email, String phonenumber, String sex)
    {
        User currentUser = getSysUser();
        currentUser.setUserName(username);
        currentUser.setEmail(email);
        currentUser.setPhonenumber(phonenumber);
        currentUser.setSex(sex);

        if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(currentUser, currentUser.getTenantCode()))) {
            return error("修改用户'" + currentUser.getUserName() + "'失败，手机号码已存在");
        } else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(currentUser, currentUser.getTenantCode()))) {
            return error("修改用户'" + currentUser.getUserName() + "'失败，邮箱账号已存在");
        }

        if (userService.updateUserInfo(currentUser) > 0)
        {
            setSysUser(userService.selectUserById(currentUser.getUserId()));
            return success();
        }
        return error();
    }

    @ApiOperation(value = "用户头像修改")
    @ApiImplicitParam(name = "avatarfile", value = "头像文件", required = true, dataType = "file", paramType = "query")
    @PostMapping("/updateAvatar")
    public AjaxResult updateAvatar(@RequestParam("avatarfile") MultipartFile avatarfile)
    {
        User user = getSysUser();
        try
        {
            if (!avatarfile.isEmpty())
            {
                String avatar = FileUploadUtils.upload(ZhangqiangCloudConfig.getAvatarPath(), avatarfile);
                user.setAvatar(avatar);
                if (userService.updateUserInfo(user) > 0)
                {
                    setSysUser(userService.selectUserById(user.getUserId()));
                    return success();
                }
            }
            return error();
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    @ApiOperation(value = "用户密码重置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/resetPwd")
    public AjaxResult resetPwd(String oldPassword, String newPassword)
    {
        User user = getSysUser();
        if (StringUtils.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword))
        {
            user.setPassword(newPassword);
            if (userService.resetUserPwd(user) > 0)
            {
                setSysUser(userService.selectUserById(user.getUserId()));
                return success();
            }
            return error();
        }
        else
        {
            return error("修改密码失败，旧密码错误");
        }

    }


    @ApiOperation(value = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "portalCode", value = "租户标识", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "账户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phonenumber", value = "手机号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sex", value = "性别（0男 1女 2未知）", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping("/register")
    public AjaxResult register(String portalCode, String username, String email, String phonenumber, String password, String sex)
    {
        User user = new User();
        user.setTenantCode(portalCode);
        user.setLoginName(username);
        user.setUserName(username);
        user.setEmail(email);
        user.setPhonenumber(phonenumber);
        user.setPassword(password);
        user.setSex(sex);
        String msg = registerService.register(user);
        return org.springframework.util.StringUtils.isEmpty(msg) ? success() : error(msg);
    }




}
