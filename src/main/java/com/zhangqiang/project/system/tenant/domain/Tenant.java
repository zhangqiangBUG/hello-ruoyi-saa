package com.zhangqiang.project.system.tenant.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zhangqiang.framework.aspectj.lang.annotation.Excel;
import com.zhangqiang.framework.web.domain.BaseEntity;

/**
 * 租户信息对象 sys_tenant
 *
 * @author zhangqiang
 * @date 2020-05-18
 */
public class Tenant extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 租户编号 */
    private String tenantCode;

    /** NEW租户编号 */
    private String newTenantCode;

    /** 父级租户编号 */
    private String parentCode;

    /** 租户名称 */
    private String tenantName;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String tenantNickname;

    /** 租户邮箱 */
    @Excel(name = "租户邮箱")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 联系地址 */
    @Excel(name = "联系地址")
    private String address;

    /** 头图 */
    private String titlepath;

    /** 头图 */
    private String titlename;

    /** 背景图 */
    private String bgpath;

    /** 背景图 */
    private String bgname;

    /** 首页请求地址 */
    private String homepath;

    /** 租户状态（0正常 1停用） */
    @Excel(name = "租户状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 角色组 */
    private Long[] roleIds;

    /** 使用期限 */
    private String tenantTerm;

    /** 激活码 */
    private String tenantKey;

    public void setTenantCode(String tenantCode)
    {
        this.tenantCode = tenantCode;
    }

    public String getTenantCode()
    {
        return tenantCode;
    }

    public String getNewTenantCode() {
        return newTenantCode;
    }

    public void setNewTenantCode(String newTenantCode) {
        this.newTenantCode = newTenantCode;
    }

    public void setTenantName(String tenantName)
    {
        this.tenantName = tenantName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getTenantName()
    {
        return tenantName;
    }
    public void setTenantNickname(String tenantNickname)
    {
        this.tenantNickname = tenantNickname;
    }

    public String getTenantNickname()
    {
        return tenantNickname;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public String getTitlepath() {
        return titlepath;
    }

    public void setTitlepath(String titlepath) {
        this.titlepath = titlepath;
    }

    public String getBgpath() {
        return bgpath;
    }

    public void setBgpath(String bgpath) {
        this.bgpath = bgpath;
    }

    public String getTitlename() {
        return titlename;
    }

    public void setTitlename(String titlename) {
        this.titlename = titlename;
    }

    public String getBgname() {
        return bgname;
    }

    public void setBgname(String bgname) {
        this.bgname = bgname;
    }

    public String getHomepath() {
        return homepath;
    }

    public void setHomepath(String homepath) {
        this.homepath = homepath;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public String getTenantTerm() {
        return tenantTerm;
    }

    public void setTenantTerm(String tenantTerm) {
        this.tenantTerm = tenantTerm;
    }

    public String getTenantKey() {
        return tenantKey;
    }

    public void setTenantKey(String tenantKey) {
        this.tenantKey = tenantKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("tenantCode", getTenantCode()).append("tenantCode", getTenantCode())
                .append("parentCode", getParentCode())
                .append("tenantNickname", getTenantNickname())
                .append("email", getEmail())
                .append("phonenumber", getPhonenumber())
                .append("address", getAddress())
                .append("tenantTerm", getTenantTerm())
                .append("tenantKey", getTenantKey())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
