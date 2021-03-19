package com.zhangqiang.project.monitor.operlog.controller;

import java.util.LinkedList;
import java.util.List;

import com.zhangqiang.common.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.zhangqiang.common.utils.poi.ExcelUtil;
import com.zhangqiang.framework.aspectj.lang.annotation.Log;
import com.zhangqiang.framework.aspectj.lang.enums.BusinessType;
import com.zhangqiang.framework.web.controller.BaseController;
import com.zhangqiang.framework.web.domain.AjaxResult;
import com.zhangqiang.framework.web.page.TableDataInfo;
import com.zhangqiang.project.monitor.operlog.domain.OperLog;
import com.zhangqiang.project.monitor.operlog.service.IOperLogService;

/**
 * 操作日志记录
 *
 * @author zhangqiang
 */
@Controller
@RequestMapping("/monitor/operlog")
public class OperlogController extends BaseController {
    private String prefix = "monitor/operlog";

    @Autowired
    private IOperLogService operLogService;

    @RequiresPermissions("monitor:operlog:view")
    @GetMapping()
    public String operlog() {
        return prefix + "/operlog";
    }

    @RequiresPermissions("monitor:operlog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OperLog operLog) {
        startPage();
        List<OperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:operlog:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OperLog operLog, @RequestParam("ids") String ids) {
        // 追加导出选中的功能
        List<OperLog> list;
        if (StringUtils.isBlank(ids)) {
            list = operLogService.selectOperLogList(operLog);
        } else {
            list = new LinkedList<>();

            String[] idArray =StringUtils.split(ids, ",");
            for (String id : idArray) {
                list.add(operLogService.selectOperLogById(Long.parseLong(id)));
            }
        }

        ExcelUtil<OperLog> util = new ExcelUtil<>(OperLog.class);
        return util.exportExcel(list, "操作日志");
    }

    @RequiresPermissions("monitor:operlog:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(operLogService.deleteOperLogByIds(ids));
    }

    @RequiresPermissions("monitor:operlog:detail")
    @GetMapping("/detail/{operId}")
    public String detail(@PathVariable("operId") Long operId, ModelMap mmap) {
        mmap.put("operLog", operLogService.selectOperLogById(operId));
        return prefix + "/detail";
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @RequiresPermissions("monitor:operlog:remove")
    @PostMapping("/clean")
    @ResponseBody
    public AjaxResult clean() {
        operLogService.cleanOperLog();
        return success();
    }
}
