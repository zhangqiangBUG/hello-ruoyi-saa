package com.zhangqiang.project.system.notice.domain;

import com.zhangqiang.framework.aspectj.lang.annotation.Excel;
import com.zhangqiang.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 通知与用户关联对象 sys_user_notice
 * 
 * @author zhangqiang
 * @date 2020-07-10
 */
public class UserNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long noticeId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 类型 0 通知 */
    @Excel(name = "类型 0 通知")
    private int type;

    /** 状态：0待办 1已办 */
    @Excel(name = "状态：0待办 1已办")
    private int state;

    public void setNoticeId(Long noticeId) 
    {
        this.noticeId = noticeId;
    }

    public Long getNoticeId() 
    {
        return noticeId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }
    public void setState(int state)
    {
        this.state = state;
    }

    public int getState()
    {
        return state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("userId", getUserId())
            .append("type", getType())
            .append("state", getState())
            .toString();
    }
}
