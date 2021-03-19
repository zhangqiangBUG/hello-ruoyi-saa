package com.zhangqiang.framework.shiro.service;

import com.zhangqiang.common.constant.Constants;
import com.zhangqiang.common.constant.ShiroConstants;
import com.zhangqiang.common.constant.UserConstants;
import com.zhangqiang.common.exception.BusinessException;
import com.zhangqiang.common.exception.user.*;
import com.zhangqiang.common.utils.AESUtil;
import com.zhangqiang.common.utils.DateUtils;
import com.zhangqiang.common.utils.MessageUtils;
import com.zhangqiang.common.utils.ServletUtils;
import com.zhangqiang.common.utils.security.ShiroUtils;
import com.zhangqiang.framework.manager.AsyncManager;
import com.zhangqiang.framework.manager.factory.AsyncFactory;
import com.zhangqiang.project.system.tenant.domain.Tenant;
import com.zhangqiang.project.system.tenant.service.ITenantService;
import com.zhangqiang.project.system.user.domain.User;
import com.zhangqiang.project.system.user.domain.UserStatus;
import com.zhangqiang.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 登录校验方法
 *
 * @author zhangqiang
 */
@Component
public class LoginService {
    @Autowired
    private PasswordService passwordService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITenantService tenantService;

    /**
     * 登录
     */
    public User login(String username, String password) {
        // 企业编号
        Object tenantReq = ServletUtils.getRequest().getSession().getAttribute(ShiroConstants.TENANT_CODE);
        String tenantcode = tenantReq==null?"":tenantReq.toString();
        if (StringUtils.isEmpty(tenantcode)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("user.portalCode.null")));
            throw new BusinessException(MessageUtils.message("user.portalCode.null"));
        }
        Tenant tenant = tenantService.selectTenantById(tenantcode);
        if (tenant == null || !"0".equals(tenant.getStatus())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("user.portalCode.error")));
            throw new BusinessException(MessageUtils.message("user.portalCode.error"));
        }
        String tenantkey = AESUtil.decrypt(tenant.getTenantKey(),Constants.TENANT_TERM_KEY+tenant.getTenantCode());
        if(StringUtils.isEmpty(tenantkey)){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("user.portalCode.term.error")));
            throw new BusinessException(MessageUtils.message("user.portalCode.term.error"));
        }
        String[] tenantkey_arr = tenantkey.split(Constants.TENANT_TERM_SPLIT);
        if(tenantkey_arr.length<2 || !tenantcode.equals(tenantkey_arr[0])){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("user.portalCode.term.error")));
            throw new BusinessException(MessageUtils.message("user.portalCode.term.error"));
        }
        if(!"0".equals(tenantkey_arr[1]) && DateUtils.parseDate(tenantkey_arr[1]).getTime() < DateUtils.getNowDate().getTime()){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("user.portalCode.term.expire")));
            throw new BusinessException(MessageUtils.message("user.portalCode.term.expire"));
        }

        // 验证码校验
        if (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // 查询用户信息
        User user = userService.selectUserByLoginName(username, tenantcode);

        if (user == null && maybeMobilePhoneNumber(username)) {
            user = userService.selectUserByPhoneNumber(username, tenantcode);
        }

        if (user == null && maybeEmail(username)) {
            user = userService.selectUserByEmail(username, tenantcode);
        }

        if (user == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
            throw new UserNotExistsException();
        }

        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete")));
            throw new UserDeleteException();
        }

        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked", user.getRemark())));
            throw new UserBlockedException();
        }

        passwordService.validate(user, password);

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(tenantcode, username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        recordLoginInfo(user);
        return user;
    }

    private boolean maybeEmail(String username) {
        if (!username.matches(UserConstants.EMAIL_PATTERN)) {
            return false;
        }
        return true;
    }

    private boolean maybeMobilePhoneNumber(String username) {
        if (!username.matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN)) {
            return false;
        }
        return true;
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(User user) {
        user.setLoginIp(ShiroUtils.getIp());
        user.setLoginDate(DateUtils.getNowDate());
        userService.updateUserInfo(user);
    }
}
