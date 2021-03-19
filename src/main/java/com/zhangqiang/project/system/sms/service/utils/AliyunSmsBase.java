package com.zhangqiang.project.system.sms.service.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zhangqiang.framework.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AliyunSmsBase {

    @Value("${spring.sms.aliyun.product}")
    private String product;

    @Value("${spring.sms.aliyun.domain}")
    private String domain;

    @Value("${spring.sms.aliyun.signName}")
    private String signName;

    @Value("${spring.sms.aliyun.AccessKeyID}")
    private String AccessKeyID;

    @Value("${spring.sms.aliyun.AccessKeySecret}")
    private String AccessKeySecret;

    @Value("${spring.sms.aliyun.template_vcode}")
    private String template_vcode;

    /**
     * 发送通知短信
     * @param phone 手机号码
     * @param type 短信模板类型
     * @param params 参数
     */
    public AjaxResult sendNoticeSms(String phone, String type, String params) {
        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", AccessKeyID, AccessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(phone);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(type);

            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(params);

            //发送短信
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                //请求成功
//                logger.info("发送通知短信[{}][{}]成功：{}", phone, type);
                return AjaxResult.success();
            } else {
//                logger.info("发送通知短信[{}][{}]失败：{}", phone, type, sendSmsResponse.getCode());
                return AjaxResult.error(sendSmsResponse.getMessage());
            }
        } catch (Exception e) {
//            logger.error("发送通知短信[{}][{}]失败：{}", phone, type, e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

}
