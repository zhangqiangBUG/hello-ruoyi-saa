package com.zhangqiang.project.system.user.controller;

import com.zhangqiang.common.constant.ShiroConstants;
import com.zhangqiang.framework.web.controller.BaseController;
import com.zhangqiang.framework.web.domain.AjaxResult;
import com.zhangqiang.project.system.sms.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 忘记密码
 * 
 * @author zhangqiang
 */
@Controller
@RequestMapping("/hotel/forgotpsw")
public class ForgotPswController extends BaseController
{

    @Autowired
    private EmailService emailService;

    /**
     * 密码找回页面
     * @return
     */
    @GetMapping("/{portalCode}/pswretrieve")
    public String pswRetrieve(HttpServletRequest request, HttpServletResponse response,@PathVariable String portalCode)
    {
        request.getSession().setAttribute(ShiroConstants.TENANT_CODE,portalCode);
        return "pswretrieve";
    }

    /**
     * 密码找回
     * @param mail
     * @param portalCode
     * @return
     */
//    @Log(title = "密码找回邮件" , businessType = BusinessType.OTHER)
    @PostMapping("/{portalCode}/pswretrieve")
    @ResponseBody
    public AjaxResult ajaxPswretrieve(@PathVariable String portalCode,String mail)
    {
        return emailService.pswRetrieve(mail,portalCode);
    }

    /**
     * 忘记密码修改页面
     * @return
     */
    @GetMapping("/{portalCode}/pswmodify")
    public String pswmodify(ModelMap mmap,HttpServletRequest request, HttpServletResponse response, @PathVariable String portalCode, String mail)
    {
        request.getSession().setAttribute(ShiroConstants.TENANT_CODE,portalCode);
        request.getSession().setAttribute("mail",mail);
        mmap.put("portalCode",portalCode);
        mmap.put("mail",mail);
        return "pswmodify";
    }

    /**
     * 密码修改
     * @param portalCode
     * @param mail
     * @param password
     * @return
     */
//    @Log(title = "密码重置" , businessType = BusinessType.OTHER)
    @PostMapping("/{portalCode}/pswmodify")
    @ResponseBody
    public AjaxResult ajaxPswmodify(@PathVariable String portalCode,String mail,String password)
    {
        return emailService.pswmodify(mail,portalCode,password);
    }
}
