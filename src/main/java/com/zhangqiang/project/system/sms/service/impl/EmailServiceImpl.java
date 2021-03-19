package com.zhangqiang.project.system.sms.service.impl;

import com.zhangqiang.common.constant.Constants;
import com.zhangqiang.common.utils.CacheUtils;
import com.zhangqiang.common.utils.ServletUtils;
import com.zhangqiang.framework.shiro.service.PasswordService;
import com.zhangqiang.framework.web.domain.AjaxResult;
import com.zhangqiang.project.system.sms.service.utils.EmailBase;
import com.zhangqiang.project.system.sms.service.EmailService;
import com.zhangqiang.project.system.user.domain.User;
import com.zhangqiang.project.system.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * 邮件业务层
 * 
 * @author zhangqiang
 */
@Service
public class EmailServiceImpl implements EmailService
{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private EmailBase emailBase;

    /**
     * 登录验证码发送
     * 模板格式 {tenantName} :登录验证码为{code},请妥善保存,勿告知他人
     * @param tenantName 企业名称
     * @param email 邮箱
     * @param code 验证码
     * @return
     */
    public AjaxResult sendLoginVcode(String tenantName,String email,String code){
        if(emailBase.sendEmail(email,tenantName+"登录验证码",tenantName+":登录验证码为"+code+",请妥善保管,勿告知他人")){
            CacheUtils.remove(Constants.SYS_CCODE_CACHE,Constants.SYS_CODE_HEADER+email);
            CacheUtils.put(Constants.SYS_CCODE_CACHE,Constants.SYS_CODE_HEADER+email,code);
            return AjaxResult.success();
        }
        return AjaxResult.error("验证码发送失败");
    }

    /**
     * 找回密码 邮件发送
     *
     * @param mail 邮箱
     * @param portalCode 企业标识
     * @return 用户对象信息
     */
    public AjaxResult pswRetrieve(String mail,String portalCode){
        // 设置邮件内容
        HttpServletRequest request = ServletUtils.getRequest();
        String url = request.getHeader("referer");
        String appUrl = url.substring(0,url.indexOf(request.getServletPath()));
        String content = "您正在申请重置密码，请点击此链接重置密码: \n" + appUrl + "/hotel/forgotpsw/"+portalCode+"/pswmodify?mail=" + mail;
        if(!emailBase.sendEmail(mail,"制卡系统密码找回",content)){
            return AjaxResult.error("邮件发送失败！");
        }
        CacheUtils.remove(Constants.SYS_ORDER_CACHE,portalCode+"_"+mail);
        CacheUtils.put(Constants.SYS_ORDER_CACHE,portalCode+"_"+mail,mail);
        return AjaxResult.success("发送成功");
    }

    /**
     * 找回密码 邮件发送
     *
     * @param mail 邮箱
     * @param portalCode 企业标识
     * @param password 修改密码
     * @return 用户对象信息
     */
    public AjaxResult pswmodify(String mail, String portalCode, String password){
        Object mailo = CacheUtils.get(Constants.SYS_ORDER_CACHE,portalCode+"_"+mail);
        if(mailo == null){
            return AjaxResult.error("重置链接已过期！");
        }
        CacheUtils.remove(Constants.SYS_ORDER_CACHE,portalCode+"_"+mail);
        User user = userMapper.selectUserByEmail(mail,portalCode);
        if(user == null){
            return AjaxResult.error("邮箱用户不存在！");
        }
        user.randomSalt();
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), password, user.getSalt()));
        userMapper.updateUser(user);
        return AjaxResult.success("重置成功");
    }
}
