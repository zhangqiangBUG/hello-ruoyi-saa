package com.zhangqiang.project.monitor.logininfor.service;

import java.util.List;

import com.zhangqiang.common.constant.ShiroConstants;
import com.zhangqiang.common.utils.ServletUtils;
import com.zhangqiang.common.utils.StringUtils;
import com.zhangqiang.common.utils.security.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhangqiang.common.utils.text.Convert;
import com.zhangqiang.project.monitor.logininfor.domain.Logininfor;
import com.zhangqiang.project.monitor.logininfor.mapper.LogininforMapper;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author zhangqiang
 */
@Service
public class LogininforServiceImpl implements ILogininforService
{
    @Autowired
    private LogininforMapper logininforMapper;

    /**
     * 新增系统登录日志
     * 
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(Logininfor logininfor)
    {
        logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * 查询系统登录日志集合
     * 
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<Logininfor> selectLogininforList(Logininfor logininfor)
    {
        logininfor.setTenantCode(ShiroUtils.getSysUser().getTenantCode());
        return logininforMapper.selectLogininforList(logininfor);
    }

    @Override
    public Logininfor selectLogininforById(Long id) {
        return logininforMapper.selectLogininforById(id);
    }

    /**
     * 批量删除系统登录日志
     * 
     * @param ids 需要删除的数据
     * @return
     */
    @Override
    public int deleteLogininforByIds(String ids)
    {
        return logininforMapper.deleteLogininforByIds(Convert.toStrArray(ids));
    }
    
    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor()
    {
        logininforMapper.cleanLogininfor();
    }
}
