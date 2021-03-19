package com.zhangqiang.project.tool.swagger;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zhangqiang.framework.web.controller.BaseController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * swagger 接口
 * 
 * @author zhangqiang
 */
@Controller
@RequestMapping("/tool/swagger")
public class SwaggerController extends BaseController
{
    @RequiresPermissions("tool:swagger:view")
    @GetMapping()
    public ModelAndView index()
    {
        String url = "/swagger-ui.html"; // redirect("/swagger-ui.html");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView(url,true,false));
        return modelAndView;
    }
}
