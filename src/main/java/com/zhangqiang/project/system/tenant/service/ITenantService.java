package com.zhangqiang.project.system.tenant.service;

import java.util.List;
import com.zhangqiang.project.system.tenant.domain.Tenant;

/**
 * 租户信息Service接口
 * 
 * @author zhangqiang
 * @date 2020-05-18
 */
public interface ITenantService 
{
    /**
     * 查询租户信息
     * 
     * @param tenantCode 租户信息ID
     * @return 租户信息
     */
    public Tenant selectTenantById(String tenantCode);

    /**
     * 查询租户信息列表
     * 
     * @param tenant 租户信息
     * @return 租户信息集合
     */
    public List<Tenant> selectTenantList(Tenant tenant);

    /**
     * 新增租户信息
     * 
     * @param tenant 租户信息
     * @return 结果
     */
    public int insertTenant(Tenant tenant);

    /**
     * 新增租户管理员信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int insertTenantAdmin(Tenant tenant);

    /**
     * 修改租户信息
     * 
     * @param tenant 租户信息
     * @return 结果
     */
    public int updateTenant(Tenant tenant);

    /**
     * 批量删除租户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTenantByIds(String ids);

    /**
     * 删除租户信息信息
     * 
     * @param tenantCode 租户信息ID
     * @return 结果
     */
    public int deleteTenantById(String tenantCode);
}
