package com.zhangqiang.framework.shiro.web.filter;

import com.zhangqiang.common.utils.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

public class UserFilter extends AccessControlFilter {
    /**
     * 强制退出后重定向的地址
     */
    @Value("${shiro.user.loginUrl}")
    private String loginUrl;

    public UserFilter() {
    }

    public String getLoginUrl() {
        return this.loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if (this.isLoginRequest(request, response)) {
            return true;
        } else {
            Subject subject = this.getSubject(request, response);
            if(subject.getPrincipal() == null){
                return false;
            }
            return true;
        }
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        saveRequest(request);
        String url = loginUrl;
        String[] request_urls = ((ShiroHttpServletRequest) request).getRequestURI().split("/");
        if (StringUtils.isNotEmpty(request_urls) && request_urls.length>=2)
        {
            url = "/"+request_urls[request_urls.length-2]+loginUrl;
        }
        WebUtils.issueRedirect(request, response, url,(Map)null, true, false);
        return false;
    }

}
