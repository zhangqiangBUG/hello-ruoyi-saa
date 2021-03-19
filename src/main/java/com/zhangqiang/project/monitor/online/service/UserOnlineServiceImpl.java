package com.zhangqiang.project.monitor.online.service;

import java.io.Serializable;
import java.util.Date;
import java.util.Deque;
import java.util.List;

import com.zhangqiang.common.constant.ShiroConstants;
import com.zhangqiang.common.utils.CacheUtils;
import com.zhangqiang.common.utils.security.ShiroUtils;
import com.zhangqiang.project.monitor.online.domain.OnlineSession;
import com.zhangqiang.project.system.user.domain.User;
import com.zhangqiang.project.system.user.mapper.UserMapper;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhangqiang.common.utils.DateUtils;
import com.zhangqiang.common.utils.StringUtils;
import com.zhangqiang.framework.shiro.session.OnlineSessionDAO;
import com.zhangqiang.project.monitor.online.domain.UserOnline;
import com.zhangqiang.project.monitor.online.mapper.UserOnlineMapper;

/**
 * 在线用户 服务层处理
 * 
 * @author zhangqiang
 */
@Service
public class UserOnlineServiceImpl implements IUserOnlineService
{
    @Autowired
    private UserOnlineMapper userOnlineDao;

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    @Autowired
    private UserMapper userDao;

    /**
     * 通过会话序号查询信息
     * 
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    @Override
    public UserOnline selectOnlineById(String sessionId)
    {
        return userOnlineDao.selectOnlineById(sessionId);
    }

    /**
     * 通过会话序号删除信息
     * 
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    @Override
    public void deleteOnlineById(String sessionId)
    {
        UserOnline userOnline = selectOnlineById(sessionId);
        if (StringUtils.isNotNull(userOnline))
        {
            userOnlineDao.deleteOnlineById(sessionId);
        }
    }

    /**
     * 通过会话序号删除信息
     * 
     * @param sessions 会话ID集合
     * @return 在线用户信息
     */
    @Override
    public void batchDeleteOnline(List<String> sessions)
    {
        for (String sessionId : sessions)
        {
            UserOnline userOnline = selectOnlineById(sessionId);
            if (StringUtils.isNotNull(userOnline))
            {
                userOnlineDao.deleteOnlineById(sessionId);
            }
        }
    }

    /**
     * 保存会话信息
     * 
     * @param online 会话信息
     */
    @Override
    public void saveOnline(UserOnline online)
    {
        if(StringUtils.isEmpty(online.getTenantCode())){
            OnlineSession os = online.getSession();
            if(os!=null && os.getUserId()!=null ){
                User user = userDao.selectUserById(os.getUserId());
                online.setTenantCode(user == null?"":user.getTenantCode());
            }
        }
        userOnlineDao.saveOnline(online);
    }

    /**
     * 查询会话集合
     * 
     * @param userOnline
     */
    @Override
    public List<UserOnline> selectUserOnlineList(UserOnline userOnline)
    {
        userOnline.setTenantCode(ShiroUtils.getSysUser().getTenantCode());
        return userOnlineDao.selectUserOnlineList(userOnline);
    }

    /**
     * 强退用户
     * 
     * @param sessionId 会话ID
     */
    @Override
    public void forceLogout(String sessionId)
    {
        Session session = onlineSessionDAO.readSession(sessionId);
        if (session == null)
        {
            return;
        }
        session.setTimeout(1000);
        userOnlineDao.deleteOnlineById(sessionId);
    }


    /**
     * 强退用户
     *
     * @param user 用户
     */
    @Override
    public void forceLogout(User user){
        Deque<Serializable> deque = (Deque<Serializable>)CacheUtils.get(ShiroConstants.SYS_USERCACHE,user.getTenantCode()+"_"+user.getLoginName());
        if (deque != null){
            while (deque.size() > 0){
                Serializable sessionId = deque.poll();
                UserOnline online = selectOnlineById(sessionId.toString());
                if (online == null)
                {
                    return ;
                }
                OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
                if (onlineSession == null)
                {
                    return ;
                }
                // 设置会话的kickout属性表示踢出了
//                onlineSession.setAttribute("kickout", true);
                onlineSession.setStatus(OnlineSession.OnlineStatus.off_line);
                onlineSessionDAO.update(onlineSession);
                online.setStatus(OnlineSession.OnlineStatus.off_line);
                saveOnline(online);
            }
        }
    }

    /**
     * 查询会话集合
     * 
     * @param expiredDate
     */
    @Override
    public List<UserOnline> selectOnlineByExpired(Date expiredDate)
    {
        String lastAccessTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, expiredDate);
        return userOnlineDao.selectOnlineByExpired(lastAccessTime);
    }
}
