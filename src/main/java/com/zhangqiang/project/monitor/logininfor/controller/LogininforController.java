package com.zhangqiang.project.monitor.logininfor.controller;

import java.util.LinkedList;
import java.util.List;

import com.zhangqiang.common.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.zhangqiang.common.utils.poi.ExcelUtil;
import com.zhangqiang.framework.aspectj.lang.annotation.Log;
import com.zhangqiang.framework.aspectj.lang.enums.BusinessType;
import com.zhangqiang.framework.shiro.service.PasswordService;
import com.zhangqiang.framework.web.controller.BaseController;
import com.zhangqiang.framework.web.domain.AjaxResult;
import com.zhangqiang.framework.web.page.TableDataInfo;
import com.zhangqiang.project.monitor.logininfor.domain.Logininfor;
import com.zhangqiang.project.monitor.logininfor.service.ILogininforService;

/**
 * 系统访问记录
 *
 * @author zhangqiang
 */
@Controller
@RequestMapping("/monitor/logininfor")
public class LogininforController extends BaseController {
    private String prefix = "monitor/logininfor";

    @Autowired
    private ILogininforService logininforService;

    @Autowired
    private PasswordService passwordService;

    @RequiresPermissions("monitor:logininfor:view")
    @GetMapping()
    public String logininfor() {
        return prefix + "/logininfor";
    }

    @RequiresPermissions("monitor:logininfor:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Logininfor logininfor) {
        startPage();
        List<Logininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    @Log(title = "登陆日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:logininfor:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Logininfor logininfor, @RequestParam("ids") String ids) {
        // 追加导出选中的功能
        List<Logininfor> list;
        if (StringUtils.isBlank(ids)) {
            list = logininforService.selectLogininforList(logininfor);
        } else {
            list = new LinkedList<>();

            String[] idArray = StringUtils.split(ids, ",");
            for (String id : idArray) {
                list.add(logininforService.selectLogininforById(Long.parseLong(id)));
            }
        }

        ExcelUtil<Logininfor> util = new ExcelUtil<>(Logininfor.class);
        return util.exportExcel(list, "登陆日志");
    }

    @RequiresPermissions("monitor:logininfor:remove")
    @Log(title = "登陆日志", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(logininforService.deleteLogininforByIds(ids));
    }

    @RequiresPermissions("monitor:logininfor:remove")
    @Log(title = "登陆日志", businessType = BusinessType.CLEAN)
    @PostMapping("/clean")
    @ResponseBody
    public AjaxResult clean() {
        logininforService.cleanLogininfor();
        return success();
    }

    @RequiresPermissions("monitor:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @PostMapping("/unlock")
    @ResponseBody
    public AjaxResult unlock(String loginName) {
        passwordService.unlock(loginName);
        return success();
    }
}
