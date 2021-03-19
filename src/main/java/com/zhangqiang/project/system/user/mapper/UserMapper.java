package com.zhangqiang.project.system.user.mapper;

import com.zhangqiang.project.system.user.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 数据层
 * 
 * @author zhangqiang
 */
public interface UserMapper
{
    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<User> selectUserList(User user);

    /**
     * 根据条件分页查询未已配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<User> selectAllocatedList(User user);

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<User> selectUnallocatedList(User user);

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @param tenantcode 企业标识
     * @return 用户对象信息
     */
    public User selectUserByLoginName(@Param("userName") String userName, @Param("tenantcode") String tenantcode);


    /**
     * 通过租户查询超级管理员
     *
     * @param tenantcode 企业标识
     * @return 用户对象信息
     */
    public User selectUserByTenant(@Param("tenantcode") String tenantcode);


    /**
     * 通过手机号码查询用户
     * 
     * @param phoneNumber 手机号码
     * @param tenantcode 企业标识
     * @return 用户对象信息
     */
    public User selectUserByPhoneNumber(@Param("phoneNumber") String phoneNumber, @Param("tenantcode") String tenantcode);

    /**
     * 通过邮箱查询用户
     * 
     * @param email 邮箱
     * @param tenantcode 企业标识
     * @return 用户对象信息
     */
    public User selectUserByEmail(@Param("email") String email, @Param("tenantcode") String tenantcode);

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public User selectUserById(Long userId);

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserByIds(Long[] ids);

    /**
     * 修改用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(User user);

    /**
     * 新增用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(User user);

    /**
     * 校验用户名称是否唯一
     * 
     * @param loginName 登录名称
     * @return 结果
     */
    public int checkLoginNameUnique(@Param("loginName") String loginName, @Param("tenantcode") String tenantcode);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public User checkPhoneUnique(@Param("phoneNumber") String phonenumber, @Param("tenantcode") String tenantcode);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public User checkEmailUnique(@Param("email") String email, @Param("tenantcode") String tenantcode);
}
