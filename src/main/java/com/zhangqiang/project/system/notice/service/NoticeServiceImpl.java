package com.zhangqiang.project.system.notice.service;

import java.util.List;

import com.zhangqiang.project.system.user.domain.User;
import com.zhangqiang.project.system.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhangqiang.common.utils.security.ShiroUtils;
import com.zhangqiang.common.utils.text.Convert;
import com.zhangqiang.project.system.notice.mapper.UserNoticeMapper;
import com.zhangqiang.project.system.notice.mapper.NoticeMapper;
import com.zhangqiang.project.system.notice.domain.Notice;
import com.zhangqiang.project.system.notice.domain.UserNotice;
import org.springframework.transaction.annotation.Transactional;

/**
 * 公告 服务层实现
 * 
 * @author zhangqiang
 * @date 2018-06-25
 */
@Service
public class NoticeServiceImpl implements INoticeService
{
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserNoticeMapper userNoticeMapper;

    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public Notice selectNoticeById(Long noticeId)
    {
        return noticeMapper.selectNoticeById(noticeId);
    }

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    @Transactional
    public List<Notice> selectNoticeList(Notice notice)
    {
        User user = ShiroUtils.getSysUser();
        if(user!=null){
            UserNotice userNotice = new UserNotice();
            userNotice.setUserId(user.getUserId());
            userNotice.setState(1);
            userNoticeMapper.updateUserNotice(userNotice);
        }
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertNotice(Notice notice)
    {
        notice.setCreateBy(ShiroUtils.getLoginName());
        int i = noticeMapper.insertNotice(notice);
        if(i>0){
            List<User> list = userMapper.selectUserList(new User());
            for(User user:list){
                UserNotice userNotice = new UserNotice();
                userNotice.setNoticeId(notice.getNoticeId());
                userNotice.setUserId(user.getUserId());
                userNotice.setType(0);
                userNotice.setState(0);
                userNoticeMapper.insertUserNotice(userNotice);
            }
        }
        return i;
    }

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(Notice notice)
    {
        notice.setUpdateBy(ShiroUtils.getLoginName());
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 删除公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(String ids)
    {
        return noticeMapper.deleteNoticeByIds(Convert.toStrArray(ids));
    }
}
