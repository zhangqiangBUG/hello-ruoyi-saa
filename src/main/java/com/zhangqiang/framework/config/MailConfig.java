package com.zhangqiang.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * MAIL
 * 配置加载
 * 
 * @author zhangqiang
 */
@Configuration
public class MailConfig
{
    // 邮箱服务器
    @Value("${spring.mail.qq.host}")
    private String host;

    // 账号
    @Value("${spring.mail.qq.username}")
    private String username;

    // 账号名称
    @Value("${spring.mail.qq.personal}")
    private String personal;

    // 密码
    @Value("${spring.mail.qq.password}")
    private String password;

    //
    @Value("${spring.mail.qq.auth}")
    private String auth;

    //
    @Value("${spring.mail.qq.senable}")
    private String senable;

    //
    @Value("${spring.mail.qq.required}")
    private String required;

    // 字符
    @Value("${spring.mail.qq.encoding}")
    private String encoding;

    // 端口
    @Value("${spring.mail.qq.port}")
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getSenable() {
        return senable;
    }

    public void setSenable(String senable) {
        this.senable = senable;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
