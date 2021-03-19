package com.zhangqiang.framework.shiro.web.filter;

import com.zhangqiang.common.constant.Constants;
import com.zhangqiang.common.constant.ShiroConstants;
import com.zhangqiang.common.utils.MessageUtils;
import com.zhangqiang.common.utils.StringUtils;
import com.zhangqiang.common.utils.security.ShiroUtils;
import com.zhangqiang.framework.manager.AsyncManager;
import com.zhangqiang.framework.manager.factory.AsyncFactory;
import com.zhangqiang.project.system.user.domain.User;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.Map;

/**
 * 退出过滤器
 * 
 * @author zhangqiang
 */
public class LogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter
{
    private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);

    // 应用的访问路径
    @Value("${server.servlet.context-path}")
    private String contextpath;

    /**
     * 退出后重定向的地址
     */
    private String loginUrl;

    private Cache<String, Deque<Serializable>> cache;

    public String getLoginUrl()
    {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl)
    {
        this.loginUrl = loginUrl;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
    {
        try
        {
            Subject subject = getSubject(request, response);
            String redirectUrl = getRedirectUrl(request, response, subject);
            try
            {
                User user = ShiroUtils.getSysUser();
                if (StringUtils.isNotNull(user))
                {
                    String loginName = user.getLoginName();
                    // 记录用户退出日志
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getTenantCode(),loginName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
                    // 清理缓存
                    cache.remove(user.getTenantCode()+"_"+loginName);
                    redirectUrl = "/"+ user.getTenantCode() + loginUrl;
                }
                // 退出登录
                subject.logout();
            }
            catch (SessionException ise)
            {
                log.error("logout fail.", ise);
            }
//            issueRedirect(request, response, redirectUrl);
            WebUtils.issueRedirect(request, response, redirectUrl,(Map)null, true, false);
        }
        catch (Exception e)
        {
            log.error("Encountered session exception during logout.  This can generally safely be ignored.", e);
        }
        return false;
    }

    /**
     * 退出跳转URL
     */
    @Override
    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject)
    {
        String[] request_urls = ((ShiroHttpServletRequest) request).getRequestURI().split("/");
        if (StringUtils.isNotEmpty(request_urls) && request_urls.length>=2)
        {
            return "/"+request_urls[request_urls.length-2]+loginUrl;
        }
        String request_url = getLoginUrl();
        if (StringUtils.isNotEmpty(request_url))
        {
            return request_url;
        }
        return super.getRedirectUrl(request, response, subject);
    }

    // 设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager)
    {
        // 必须和ehcache缓存配置中的缓存name一致
        this.cache = cacheManager.getCache(ShiroConstants.SYS_USERCACHE);
    }
}
