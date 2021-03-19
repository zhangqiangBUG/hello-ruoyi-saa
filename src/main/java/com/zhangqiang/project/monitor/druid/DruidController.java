package com.zhangqiang.project.monitor.druid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zhangqiang.framework.web.controller.BaseController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * druid 监控
 * 
 * @author zhangqiang
 */
@Controller
@RequestMapping("/monitor/data")
public class DruidController extends BaseController
{
    private String prefix = "/druid";

    @RequiresPermissions("monitor:data:view")
    @GetMapping()
    public ModelAndView index()
    {
        String url = prefix + "/index.html";//redirect(prefix + "/index");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView(url,true,false));
        return modelAndView;
    }
}
