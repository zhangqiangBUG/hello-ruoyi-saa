package com.zhangqiang.framework.shiro.service;

import com.zhangqiang.common.constant.Constants;
import com.zhangqiang.common.constant.ShiroConstants;
import com.zhangqiang.common.constant.UserConstants;
import com.zhangqiang.common.utils.MessageUtils;
import com.zhangqiang.common.utils.ServletUtils;
import com.zhangqiang.framework.manager.AsyncManager;
import com.zhangqiang.framework.manager.factory.AsyncFactory;
import com.zhangqiang.project.system.user.domain.User;
import com.zhangqiang.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 注册校验方法
 * 
 * @author zhangqiang
 */
@Component
public class RegisterService
{
    @Autowired
    private IUserService userService;

    /**
     * 注册
     */
    public String register(User user)
    {
        String msg = "", username = user.getLoginName(), password = user.getPassword();

        String tenantcode = ServletUtils.getRequest().getAttribute(ShiroConstants.TENANT_CODE).toString();
        if(StringUtils.isEmpty(tenantcode)){
            msg = "企业编码错误";
        }
//        else if (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA)))
//        {
//            msg = "验证码错误";
//        }
        else if (StringUtils.isEmpty(username))
        {
            msg = "用户名不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
        }
        else if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(username,tenantcode)))
        {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        }
        else
        {
            user.setTenantCode(tenantcode);
            boolean regFlag = userService.registerUser(user);
            if (!regFlag)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode,username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }
}
