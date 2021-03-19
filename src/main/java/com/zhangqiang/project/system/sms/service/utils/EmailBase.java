package com.zhangqiang.project.system.sms.service.utils;

import com.zhangqiang.framework.config.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Component
public class EmailBase {

    @Autowired
    private MailConfig mailConfig;

    /**
     * 邮件发送
     * @param emailTo 收件人
     * @param title 邮件头
     * @param content 邮件内容
     * @return
     */
    public boolean sendEmail(String emailTo, String title, String content) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", mailConfig.getAuth());
        properties.put("mail.smtp.ssl.enable", mailConfig.getSenable());
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sender.setJavaMailProperties(properties);
        sender.setHost(mailConfig.getHost());
        sender.setPort(mailConfig.getPort());
        //设置用户名和密码
        sender.setUsername(mailConfig.getUsername());
        sender.setPassword(mailConfig.getPassword());
        // 1. 创建一封邮件
        MimeMessage message = sender.createMimeMessage();
        // 2. From: 发件人
        try {
            message.setFrom(new InternetAddress(mailConfig.getUsername(), mailConfig.getPersonal(), "UTF-8"));
            // 3. To: 收件人（可以增加多个收件人、抄送、密送）
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(emailTo, "普通用户", "UTF-8"));
            // 4. Subject: 邮件主题
            message.setSubject(title, mailConfig.getEncoding());
            // 5. Content: 邮件正文（可以使用html标签）
            message.setContent(content, "text/html;charset=UTF-8");
            // 6. 设置发件时间
            message.setSentDate(new Date());
            // 7. 保存设置
            message.saveChanges();
            sender.send(message);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
