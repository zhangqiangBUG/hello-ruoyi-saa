package com.zhangqiang.project.system.tenant.controller;

import java.util.List;
import java.util.Map;

import com.zhangqiang.common.utils.StringUtils;
import com.zhangqiang.common.utils.security.ShiroUtils;
import com.zhangqiang.project.system.post.service.IPostService;
import com.zhangqiang.project.system.role.service.IRoleService;
import com.zhangqiang.project.system.user.domain.User;
import com.zhangqiang.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zhangqiang.framework.aspectj.lang.annotation.Log;
import com.zhangqiang.framework.aspectj.lang.enums.BusinessType;
import com.zhangqiang.project.system.tenant.domain.Tenant;
import com.zhangqiang.project.system.tenant.service.ITenantService;
import com.zhangqiang.framework.web.controller.BaseController;
import com.zhangqiang.framework.web.domain.AjaxResult;
import com.zhangqiang.common.utils.poi.ExcelUtil;
import com.zhangqiang.framework.web.page.TableDataInfo;

/**
 * 租户信息Controller
 * 
 * @author zhangqiang
 * @date 2020-05-18
 */
@Controller
@RequestMapping("/system/tenant")
public class TenantController extends BaseController
{
    private String prefix = "system/tenant";

    @Autowired
    private ITenantService tenantService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @RequiresPermissions("system:tenant:view")
    @GetMapping()
    public String tenant()
    {
        return prefix + "/tenant";
    }

    /**
     * 查询租户信息列表
     */
    @RequiresPermissions("system:tenant:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Tenant tenant)
    {
        startPage();
        List<Tenant> list = tenantService.selectTenantList(tenant);
        return getDataTable(list);
    }

    /**
     * 导出租户信息列表
     */
    @RequiresPermissions("system:tenant:export")
    @Log(title = "租户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Tenant tenant)
    {
        List<Tenant> list = tenantService.selectTenantList(tenant);
        ExcelUtil<Tenant> util = new ExcelUtil<Tenant>(Tenant.class);
        return util.exportExcel(list, "tenant");
    }

    /**
     * 新增租户信息
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("roles", roleService.selectRoleAll());
        mmap.put("posts", postService.selectPostAll());
        return prefix + "/add";
    }

    /**
     * 新增保存租户信息
     */
    @RequiresPermissions("system:tenant:add")
    @Log(title = "租户信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Tenant tenant)
    {
        Tenant ten = tenantService.selectTenantById(tenant.getTenantCode());
        if (ten !=null && ten.getTenantCode().equals(tenant.getTenantCode())){
            return AjaxResult.error("租户标识已存在，请重新输入");
        }
        return toAjax(tenantService.insertTenant(tenant));
    }

    /**
     * 修改租户信息
     */
    @GetMapping("/edit/{tenantCode}")
    public String edit(@PathVariable("tenantCode") String tenantCode, ModelMap mmap)
    {
        User user= userService.selectUserByTenant(tenantCode);
        mmap.put("tenant", tenantService.selectTenantById(tenantCode));
        mmap.put("user",user);
        if(StringUtils.isEmpty(ShiroUtils.getSysUser().getTenant().getParentCode())){
            mmap.put("roles", user==null?roleService.selectRoleAll():roleService.selectRolesByUserId(user.getUserId()));
        }else{
            mmap.put("roles",null);
        }
        return prefix + "/edit";
    }

    /**
     * 修改保存租户信息
     */
    @RequiresPermissions("system:tenant:edit")
    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Tenant tenant)
    {
        if (StringUtils.isEmpty(tenant.getTenantCode())){
            return AjaxResult.error("租户标识不能为空");
        }
        if(!tenant.getTenantCode().equals(tenant.getTenantCode())){
            if(StringUtils.isEmpty(tenant.getParentCode())){
                return AjaxResult.error("超级租户标识不允许修改");
            }
        }
        return toAjax(tenantService.updateTenant(tenant));
    }

    /**
     * 删除租户信息
     */
    @RequiresPermissions("system:tenant:remove")
    @Log(title = "租户信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tenantService.deleteTenantByIds(ids));
    }

}
