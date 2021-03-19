package com.zhangqiang.project.system.sms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhangqiang.common.constant.Constants;
import com.zhangqiang.common.utils.CacheUtils;
import com.zhangqiang.framework.web.domain.AjaxResult;
import com.zhangqiang.project.system.sms.service.utils.AliyunSmsBase;
import com.zhangqiang.project.system.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 短信业务层
 * 
 * @author zhangqiang
 */
@Service
public class SmsServiceImpl implements SmsService
{

    @Autowired
    private AliyunSmsBase aliyunSmsBase;

    /**
     * 登录验证码发送
     * 模板格式 {tenantName} :登录验证码为{code},请妥善保存,勿告知他人
     * @param tenantName 企业名称
     * @param phone 手机
     * @param code 验证码
     * @return
     */
    public AjaxResult sendLoginVcode(String tenantName,String phone, String code){
        JSONObject params = new JSONObject();
        params.put("tenantName",tenantName);
        params.put("code",code);
        AjaxResult ajaxResult = aliyunSmsBase.sendNoticeSms(phone,"",params.toJSONString());
        if(AjaxResult.Type.SUCCESS.equals(ajaxResult.get(AjaxResult.CODE_TAG).toString())){
            CacheUtils.remove(Constants.SYS_CCODE_CACHE,Constants.SYS_CODE_HEADER+phone);
            CacheUtils.put(Constants.SYS_CCODE_CACHE,Constants.SYS_CODE_HEADER+phone,code);
        }
        return ajaxResult;
    }

}
