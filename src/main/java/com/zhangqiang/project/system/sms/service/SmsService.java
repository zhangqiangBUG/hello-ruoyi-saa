package com.zhangqiang.project.system.sms.service;

import com.zhangqiang.framework.web.domain.AjaxResult;

/**
 * 短信业务层
 * 
 * @author zhangqiang
 */
public interface SmsService
{

    /**
     * 登录验证码发送
     * @param tenantName 企业名称
     * @param phone 手机
     * @param code 验证码
     * @return
     */
    AjaxResult sendLoginVcode(String tenantName, String phone, String code);


}
