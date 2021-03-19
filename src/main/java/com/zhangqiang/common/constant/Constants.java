package com.zhangqiang.common.constant;

/**
 * 通用常量信息
 * 
 * @author zhangqiang
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";
    
    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 其他缓存 cache name
     */
    public static final String SYS_ORDER_CACHE = "sys-order";

    /**
     * 验证码缓存
     */
    public static final String SYS_CCODE_CACHE = "sys-vcode";

    /**
     * 参数管理 cache name
     */
    public static final String SYS_CONFIG_CACHE = "sys-config";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache name
     */
    public static final String SYS_DICT_CACHE = "sys-dict";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 消息管理 cache name
     */
    public static final String SYS_AGREE_MESSAGE_CACHE = "sys-agree-news";

    /**
     * 协议管理 cache name
     */
    public static final String SYS_AGREE_CACHE = "sys-agree";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 切分字符
     */
    public static final String MESSAGE_SPLIT = ",";

    /**
     * 加解密结束字符
     */
    public static final String AES_DATA_C = "#";

    /** 租户有效期限 */
    public static final String TENANT_TERM_KEY = "key";   // 加密key
    public static final String TENANT_TERM_SPLIT = ":";   // 切分字符
    public static final String TENANT_TERM_M = "30";      // 一个月
    public static final String TENANT_TERM_Y = "365";     // 一年
    public static final String TENANT_TERM_L = "0";       // 永久

    /**
     * 验证码缓存头
     */
    public static final String SYS_CODE_HEADER = "code_";


}
