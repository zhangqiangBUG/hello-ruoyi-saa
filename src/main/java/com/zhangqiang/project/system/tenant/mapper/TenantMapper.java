package com.zhangqiang.project.system.tenant.mapper;

import java.util.List;
import com.zhangqiang.project.system.tenant.domain.Tenant;
import org.apache.ibatis.annotations.Param;

/**
 * 租户信息Mapper接口
 * 
 * @author zhangqiang
 * @date 2020-05-18
 */
public interface TenantMapper 
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
     * 获取最大code
     *
     * @param parent_code 父级
     * @return 结果
     */
    public String selectMaxCode(@Param("parent_code") String parent_code);

    /**
     * 新增租户信息
     * 
     * @param tenant 租户信息
     * @return 结果
     */
    public int insertTenant(Tenant tenant);

    /**
     * 修改租户信息
     * 
     * @param tenant 租户信息
     * @return 结果
     */
    public int updateTenant(Tenant tenant);

    /**
     * 删除租户信息
     * 
     * @param tenantCode 租户信息ID
     * @return 结果
     */
    public int deleteTenantById(String tenantCode);

    /**
     * 批量删除租户信息
     * 
     * @param tenantCodes 需要删除的数据ID
     * @return 结果
     */
    public int deleteTenantByIds(String[] tenantCodes);
}
