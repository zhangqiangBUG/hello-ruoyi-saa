package com.zhangqiang.project.system.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhangqiang.common.constant.ShiroConstants;
import com.zhangqiang.common.utils.security.ShiroUtils;
import com.zhangqiang.framework.config.ZhangqiangCloudConfig;
import com.zhangqiang.project.system.tenant.domain.Tenant;
import com.zhangqiang.project.system.tenant.service.ITenantService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zhangqiang.common.utils.ServletUtils;
import com.zhangqiang.common.utils.StringUtils;
import com.zhangqiang.framework.web.controller.BaseController;
import com.zhangqiang.framework.web.domain.AjaxResult;

import java.util.Map;

/**
 * 登录验证
 * 
 * @author zhangqiang
 */
@Controller
public class LoginController extends BaseController
{
    @Autowired
    private ZhangqiangCloudConfig zhangqiangCloudConfig;
    @Autowired
    private ITenantService tenantService;

    @GetMapping("/{portalCode}/login")
    public String login(HttpServletRequest request, HttpServletResponse response,@PathVariable String portalCode, ModelMap mmap)
    {
        mmap.put("copyrightYear", zhangqiangCloudConfig.getCopyrightYear());
        mmap.put("version", zhangqiangCloudConfig.getVersion());
        mmap.put("objname", zhangqiangCloudConfig.getName());
        mmap.put("portalCode", portalCode);
        request.getSession().setAttribute(ShiroConstants.TENANT_CODE,portalCode);

        Tenant tenant = tenantService.selectTenantById(portalCode);
        if (tenant!=null){
            mmap.put("titlepath", StringUtils.isEmpty(tenant.getTitlepath())?"/zhangqiang.ico":tenant.getTitlepath());
            mmap.put("bgpath", StringUtils.isEmpty(tenant.getBgpath())?"/img/bg_login.jpg":tenant.getBgpath());
            mmap.put("tenantNickname", StringUtils.isEmpty(tenant.getTenantNickname())?"后台管理系统":tenant.getTenantNickname());

//            if(!StringUtils.isEmpty(tenant.getParentCode())){
//                String tenantkey = AESUtil.decrypt(tenant.getTenantKey(), Constants.TENANT_TERM_KEY+tenant.getTenantCode());
//                if(org.springframework.util.StringUtils.isEmpty(tenantkey))throw new BusinessException("租户激活码无效，请联系管理员");
//                String[] tenantkey_arr = tenantkey.split(Constants.TENANT_TERM_SPLIT);
//                if(tenantkey_arr.length<2 || !portalCode.equals(tenantkey_arr[0]))throw new BusinessException("租户激活码无效，请联系管理员");
//                mmap.put("tenantTerm", tenantkey_arr[1]);
//                if(DateUtils.parseDate(tenantkey_arr[1]).getTime() < DateUtils.getNowDate().getTime())throw new BusinessException("租户激活码已过期，请联系管理员");
//            }
        }else{
            mmap.put("titlepath", "/zhangqiang.ico");
            mmap.put("bgpath", "/img/bg_login.jpg");
            mmap.put("tenantNickname","后台管理系统");
        }
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request))
        {
            return ServletUtils.renderString(response, "{\"code\":\"-1\",\"msg\":\"未登录或登录超时,请重新登录.\"}");
        }
        try {
            if (ShiroUtils.getSysUser()!=null)WebUtils.issueRedirect(request, response, "/"+portalCode+"/index",(Map)null, true, false);
        }catch (Exception e){

        }
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try
        {
            token.setRememberMe(rememberMe);
            subject.login(token);
            return success();
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

    @GetMapping("/unauth")
    public String unauth()
    {
        return "error/unauth";
    }
}
