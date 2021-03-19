package com.zhangqiang.project.monitor.logininfor.service;

import java.util.List;

import com.zhangqiang.project.monitor.logininfor.domain.Logininfor;

/**
 * 系统访问日志情况信息 服务层
 *
 * @author zhangqiang
 */
public interface ILogininforService {
    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    public void insertLogininfor(Logininfor logininfor);

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    public List<Logininfor> selectLogininforList(Logininfor logininfor);

    /**
     * 根据ID查询系统登录日志
     *
     * @param id 访问日志对象的id
     * @return 登录记录集合
     */
    public Logininfor selectLogininforById(Long id);

    /**
     * 批量删除系统登录日志
     *
     * @param ids 需要删除的数据
     * @return
     */
    public int deleteLogininforByIds(String ids);

    /**
     * 清空系统登录日志
     */
    public void cleanLogininfor();
}
