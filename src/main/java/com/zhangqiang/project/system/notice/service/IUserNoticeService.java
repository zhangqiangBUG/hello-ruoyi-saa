package com.zhangqiang.project.system.notice.service;

import java.util.List;
import com.zhangqiang.project.system.notice.domain.UserNotice;

/**
 * 通知与用户关联Service接口
 * 
 * @author zhangqiang
 * @date 2020-07-10
 */
public interface IUserNoticeService 
{
    /**
     * 查询通知与用户关联
     * 
     * @param noticeId 通知与用户关联ID
     * @return 通知与用户关联
     */
    public UserNotice selectUserNoticeById(Long noticeId);

    /**
     * 查询通知与用户关联列表
     * 
     * @param userNotice 通知与用户关联
     * @return 通知与用户关联集合
     */
    public List<UserNotice> selectUserNoticeList(UserNotice userNotice);

    /**
     * 新增通知与用户关联
     * 
     * @param userNotice 通知与用户关联
     * @return 结果
     */
    public int insertUserNotice(UserNotice userNotice);

    /**
     * 修改通知与用户关联
     * 
     * @param userNotice 通知与用户关联
     * @return 结果
     */
    public int updateUserNotice(UserNotice userNotice);

    /**
     * 批量删除通知与用户关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserNoticeByIds(String ids);

    /**
     * 删除通知与用户关联信息
     * 
     * @param noticeId 通知与用户关联ID
     * @return 结果
     */
    public int deleteUserNoticeById(Long noticeId);
}
