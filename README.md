# hello-ruoyi-saas

#### 介绍
在若依开源未分离版的基础上增加租户概念，实现saas平台


#### 软件架构
在若依权限管理系统未分离版本上增加扩展功能   
1.短信、邮箱服务  
2.MQTT消息中间件  
3.第三方缓存服务  
4.扩展为SAAS平台，支持多租户管理

####使用说明
1.克隆本项目  
2.执行init_sys.sql到mysql数据库  
3.修改application-druid.yml里的数据库配置，改为你自己的  
4.idea启动工程  
  
租户登陆地址：http://localhost:8088/sys/guest/login    
账号密码：admin 111111  

平台管理员登陆地址：http://localhost:8088/sys/sys/login  
账号密码：admin admin123  

####在线体验
体验地址：http://118.89.90.121:8088/sys/guest/login  
账号密码：admin 111111


#### 链接
RuoYi是一款基于SpringBoot+Bootstrap的极速后台开发框架。  
RuoYi 官网地址：http://ruoyi.vip  
RuoYi 在线文档：http://doc.ruoyi.vip  
RuoYi 是一个 Java EE 企业级快速开发平台，基于经典技术组合（Spring Boot、Apache Shiro、MyBatis、Thymeleaf、Bootstrap），内置模块如：部门管理、角色用户、菜单及按钮授权、数据权限、系统参数、日志管理、通知公告等。在线定时任务配置；支持集群，支持多数据源，支持分布式事务。
