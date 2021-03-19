package com.zhangqiang.project.system.user.controller;

import com.zhangqiang.common.constant.Constants;
import com.zhangqiang.common.utils.AESUtil;
import com.zhangqiang.common.utils.DateUtils;
import com.zhangqiang.common.utils.MessageUtils;
import com.zhangqiang.common.utils.StringUtils;
import com.zhangqiang.framework.config.ZhangqiangCloudConfig;
import com.zhangqiang.framework.web.controller.BaseController;
import com.zhangqiang.project.system.config.service.IConfigService;
import com.zhangqiang.project.system.menu.domain.Menu;
import com.zhangqiang.project.system.menu.service.IMenuService;
import com.zhangqiang.project.system.notice.domain.UserNotice;
import com.zhangqiang.project.system.notice.service.IUserNoticeService;
import com.zhangqiang.project.system.tenant.domain.Tenant;
import com.zhangqiang.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 首页 业务处理
 * 
 * @author zhangqiang
 */
@Controller
public class IndexController extends BaseController
{
    @Autowired
    private IMenuService menuService;

    @Autowired
    private IConfigService configService;

    @Autowired
    private ZhangqiangCloudConfig zhangqiangCloudConfig;
    @Autowired
    private IUserNoticeService userNoticeService;

    // 系统首页
    @GetMapping("/{portalCode}/index")
    public String index(HttpServletRequest request, ModelMap mmap)
    {
        // 取身份信息
        User user = getSysUser();
        Tenant tenant = user.getTenant();
        if(StringUtils.isEmpty(tenant.getBgpath())){tenant.setBgpath("/img/bg_login.jpg");}
        if(StringUtils.isEmpty(tenant.getTitlepath())){tenant.setTitlepath("/zhangqiang.ico");}
        if(StringUtils.isEmpty(tenant.getTenantNickname())){tenant.setTenantNickname("后台管理系统");}
        // 租户有效期
        mmap.put("tenantTerm", "0");
        String tenantkey = AESUtil.decrypt(tenant.getTenantKey(), Constants.TENANT_TERM_KEY+tenant.getTenantCode());
        if(StringUtils.isNotEmpty(tenantkey)){
            String[] tenantkey_arr = tenantkey.split(Constants.TENANT_TERM_SPLIT);
            if(tenantkey_arr.length>1 && tenant.getTenantCode().equals(tenantkey_arr[0]) && !"0".equals(tenantkey_arr[1])){
                long dats_c = DateUtils.parseDate(tenantkey_arr[1]).getTime() - DateUtils.getNowDate().getTime();
                long days = dats_c/(1000*3600*24);
                long days_y = dats_c%(1000*3600*24);
                mmap.put("tenantTerm",
                        (dats_c<0? MessageUtils.message("no.sys.term.end.permission"):
                                (days>0?(MessageUtils.message("no.sys.term.day.permission",days)):
                                        MessageUtils.message("no.sys.term.last.permission")
                                )
                        )
                );
            }
        }

        // 根据用户id取出菜单
        List<Menu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        mmap.put("copyrightYear", zhangqiangCloudConfig.getCopyrightYear());
        mmap.put("version", zhangqiangCloudConfig.getVersion());
        mmap.put("objname", zhangqiangCloudConfig.getName());
        mmap.put("demoEnabled", zhangqiangCloudConfig.isDemoEnabled());
        mmap.put("usernotice",userNoticeService.selectUserNoticeList(new UserNotice()).size());
        return "index";
    }

    // 切换主题
    @GetMapping("/system/switchSkin")
    public String switchSkin(ModelMap mmap)
    {
        return "skin";
    }

    // 默认系统首页
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        // 取身份信息
        User user = getSysUser();
        mmap.put("user", user);
        mmap.put("version", zhangqiangCloudConfig.getVersion());
        return "main_v2";
    }
}
