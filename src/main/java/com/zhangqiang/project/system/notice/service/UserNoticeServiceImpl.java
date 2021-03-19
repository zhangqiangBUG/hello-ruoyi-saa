package com.zhangqiang.project.system.notice.service;

import java.util.List;

import com.zhangqiang.common.utils.security.ShiroUtils;
import com.zhangqiang.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhangqiang.project.system.notice.mapper.UserNoticeMapper;
import com.zhangqiang.project.system.notice.domain.UserNotice;
import com.zhangqiang.common.utils.text.Convert;

/**
 * 通知与用户关联Service业务层处理
 * 
 * @author zhangqiang
 * @date 2020-07-10
 */
@Service
public class UserNoticeServiceImpl implements IUserNoticeService 
{
    @Autowired
    private UserNoticeMapper userNoticeMapper;

    /**
     * 查询通知与用户关联
     * 
     * @param noticeId 通知与用户关联ID
     * @return 通知与用户关联
     */
    @Override
    public UserNotice selectUserNoticeById(Long noticeId)
    {
        return userNoticeMapper.selectUserNoticeById(noticeId);
    }

    /**
     * 查询通知与用户关联列表
     * 
     * @param userNotice 通知与用户关联
     * @return 通知与用户关联
     */
    @Override
    public List<UserNotice> selectUserNoticeList(UserNotice userNotice)
    {
        User user = ShiroUtils.getSysUser();
        if (user!=null)userNotice.setUserId(user.getUserId());
        userNotice.setState(0);
        return userNoticeMapper.selectUserNoticeList(userNotice);
    }

    /**
     * 新增通知与用户关联
     * 
     * @param userNotice 通知与用户关联
     * @return 结果
     */
    @Override
    public int insertUserNotice(UserNotice userNotice)
    {
        return userNoticeMapper.insertUserNotice(userNotice);
    }

    /**
     * 修改通知与用户关联
     * 
     * @param userNotice 通知与用户关联
     * @return 结果
     */
    @Override
    public int updateUserNotice(UserNotice userNotice)
    {
        return userNoticeMapper.updateUserNotice(userNotice);
    }

    /**
     * 删除通知与用户关联对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserNoticeByIds(String ids)
    {
        return userNoticeMapper.deleteUserNoticeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除通知与用户关联信息
     * 
     * @param noticeId 通知与用户关联ID
     * @return 结果
     */
    @Override
    public int deleteUserNoticeById(Long noticeId)
    {
        return userNoticeMapper.deleteUserNoticeById(noticeId);
    }
}
