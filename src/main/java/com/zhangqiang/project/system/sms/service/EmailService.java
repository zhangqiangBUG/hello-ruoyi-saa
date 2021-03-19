package com.zhangqiang.project.system.sms.service;

import com.zhangqiang.framework.web.domain.AjaxResult;

/**
 * 邮件业务层
 * 
 * @author zhangqiang
 */
public interface EmailService
{

    /**
     * 登录验证码发送
     * @param tenantName 企业名称
     * @param email 邮箱
     * @param code 验证码
     * @return
     */
    AjaxResult sendLoginVcode(String tenantName, String email, String code);

    /**
     * 找回密码 邮件发送
     *
     * @param mail 邮箱
     * @param portalCode 企业标识
     * @return 用户对象信息
     */
    AjaxResult pswRetrieve(String mail, String portalCode);

    /**
     * 找回密码 邮件发送
     *
     * @param mail 邮箱
     * @param portalCode 企业标识
     * @param password 修改密码
     * @return 用户对象信息
     */
    AjaxResult pswmodify(String mail, String portalCode, String password);

}
