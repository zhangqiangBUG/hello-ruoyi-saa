package com.zhangqiang.project.system.tenant.service.impl;

import java.util.List;
import java.util.Map;

import com.zhangqiang.common.constant.Constants;
import com.zhangqiang.common.constant.UserConstants;
import com.zhangqiang.common.exception.BusinessException;
import com.zhangqiang.common.utils.AESUtil;
import com.zhangqiang.common.utils.DateUtils;
import com.zhangqiang.common.utils.StringUtils;
import com.zhangqiang.common.utils.security.ShiroUtils;
import com.zhangqiang.framework.shiro.service.PasswordService;
import com.zhangqiang.project.system.user.domain.User;
import com.zhangqiang.project.system.user.mapper.UserMapper;
import com.zhangqiang.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhangqiang.project.system.tenant.mapper.TenantMapper;
import com.zhangqiang.project.system.tenant.domain.Tenant;
import com.zhangqiang.project.system.tenant.service.ITenantService;
import com.zhangqiang.common.utils.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户信息Service业务层处理
 * 
 * @author zhangqiang
 * @date 2020-05-18
 */
@Service
public class TenantServiceImpl implements ITenantService 
{
    @Autowired
    private IUserService userService;
    @Autowired
    private TenantMapper tenantMapper;

    /**
     * 查询租户信息
     * 
     * @param tenantCode 租户信息ID
     * @return 租户信息
     */
    @Override
    public Tenant selectTenantById(String tenantCode)
    {
        return tenantMapper.selectTenantById(tenantCode);
    }

    /**
     * 查询租户信息列表
     * 
     * @param tenant 租户信息
     * @return 租户信息
     */
    @Override
    public List<Tenant> selectTenantList(Tenant tenant)
    {
        String tenantcode = ShiroUtils.getSysUser().getTenantCode();
        tenant.setParentCode(tenantcode);
        tenant.setTenantCode(tenantcode);
        return tenantMapper.selectTenantList(tenant);
    }

    /**
     * 新增租户信息
     * 
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTenant(Tenant tenant)
    {
        tenant.setStatus("0");
        tenant.setCreateBy(ShiroUtils.getLoginName());
        tenant.setParentCode(ShiroUtils.getSysUser().getTenantCode());
        if(StringUtils.isEmpty(tenant.getTenantTerm())){
            tenant.setTenantTerm(Constants.TENANT_TERM_M);
        }
        String key = ("0".equals(tenant.getTenantTerm())?tenant.getTenantTerm():DateUtils.getDateAfter(Integer.parseInt(tenant.getTenantTerm())));
        String tenantkey = AESUtil.encrypt(tenant.getTenantCode()+ Constants.TENANT_TERM_SPLIT + key,Constants.TENANT_TERM_KEY + tenant.getTenantCode());
        tenant.setTenantKey(tenantkey);
        tenantMapper.insertTenant(tenant);
        return insertTenantAdmin(tenant);
    }

    /**
     * 新增租户管理员信息
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTenantAdmin(Tenant tenant)
    {
        // 新增用户信息
        User user = new User();
        user.setUserType(UserConstants.TENANT_USER_TYPE);
        user.setUserName(UserConstants.TENANT_USER_ADMIN);
        user.setLoginName(UserConstants.TENANT_USER_ADMIN);
        user.setPassword(UserConstants.TENANT_USER_PASSWORD);
        user.setTenantCode(tenant.getTenantCode());
        user.setRoleIds(tenant.getRoleIds());
        return userService.insertUser(user);
    }

    /**
     * 修改租户信息
     * 
     * @param tenant 租户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTenant(Tenant tenant)
    {
        // 有效期限是否变更
        Map<String, Object> params = tenant.getParams();
        if(!params.isEmpty() && params.get("tenantTerm")!=null && !params.get("tenantTerm").toString().equals(tenant.getTenantTerm())){
            tenant.setTenantTerm(params.get("tenantTerm").toString());
            String key = ("0".equals(tenant.getTenantTerm())?tenant.getTenantTerm(): DateUtils.getDateAfter(Integer.parseInt(tenant.getTenantTerm())));
            String tenantkey = AESUtil.encrypt(tenant.getTenantCode()+ Constants.TENANT_TERM_SPLIT + key,Constants.TENANT_TERM_KEY + tenant.getTenantCode());
            tenant.setTenantKey(tenantkey);
        }

        tenant.setUpdateBy(ShiroUtils.getLoginName());
        int rows = tenantMapper.updateTenant(tenant);
        // 是否存在租户管理员
        User user = userService.selectUserByTenant(tenant.getTenantCode());
        if(user == null){
            // 新增租户管理员信息
            rows =  insertTenantAdmin(tenant);
        }else{
            if (tenant.getRoleIds()!=null && tenant.getRoleIds().length>0){
                user.setRoleIds(tenant.getRoleIds());
                rows =  userService.updateUser(user);
            }
        }
        return rows;
    }

    /**
     * 删除租户信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTenantByIds(String ids)
    {
        String[] tenants = Convert.toStrArray(ids);
        for (String id:tenants)
        {
            Tenant tenant = tenantMapper.selectTenantById(id);
            if(tenant!=null && StringUtils.isEmpty(tenant.getParentCode())){
                throw new BusinessException("不允许操作超级租户");
            }
            if(tenant.getTenantCode().equals(ShiroUtils.getSysUser().getTenantCode())){
                throw new BusinessException("不允许操作当前租户");
            }
        }
        return tenantMapper.deleteTenantByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除租户信息信息
     * 
     * @param tenantCode 租户信息ID
     * @return 结果
     */
    @Override
    public int deleteTenantById(String tenantCode)
    {
        Tenant tenant = tenantMapper.selectTenantById(tenantCode);
        if(tenant.getTenantCode().equals(ShiroUtils.getSysUser().getTenantCode())){
            throw new BusinessException("不允许操作当前租户");
        }
        return tenantMapper.deleteTenantById(tenantCode);
    }




}
