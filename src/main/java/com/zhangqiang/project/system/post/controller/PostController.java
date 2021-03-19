package com.zhangqiang.project.system.post.controller;

import java.util.LinkedList;
import java.util.List;

import com.zhangqiang.common.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.zhangqiang.common.constant.UserConstants;
import com.zhangqiang.common.utils.poi.ExcelUtil;
import com.zhangqiang.framework.aspectj.lang.annotation.Log;
import com.zhangqiang.framework.aspectj.lang.enums.BusinessType;
import com.zhangqiang.framework.web.controller.BaseController;
import com.zhangqiang.framework.web.domain.AjaxResult;
import com.zhangqiang.framework.web.page.TableDataInfo;
import com.zhangqiang.project.system.post.domain.Post;
import com.zhangqiang.project.system.post.service.IPostService;

/**
 * 岗位信息操作处理
 *
 * @author zhangqiang
 */
@Controller
@RequestMapping("/system/post")
public class PostController extends BaseController {
    private String prefix = "system/post";

    @Autowired
    private IPostService postService;

    @RequiresPermissions("system:post:view")
    @GetMapping()
    public String operlog() {
        return prefix + "/post";
    }

    @RequiresPermissions("system:post:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Post post) {
        startPage();
        List<Post> list = postService.selectPostList(post);
        return getDataTable(list);
    }

    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:post:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Post post, @RequestParam("ids") String ids) {
        // 追加导出选中的功能
        List<Post> list;
        if (StringUtils.isBlank(ids)) {
            list = postService.selectPostList(post);
        } else {
            list = new LinkedList<>();

            String[] idArray =StringUtils.split(ids, ",");
            for (String id : idArray) {
                list.add(postService.selectPostById(Long.parseLong(id)));
            }
        }

        ExcelUtil<Post> util = new ExcelUtil<>(Post.class);
        return util.exportExcel(list, "岗位数据");
    }

    @RequiresPermissions("system:post:remove")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(postService.deletePostByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 新增岗位
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存岗位
     */
    @RequiresPermissions("system:post:add")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated Post post) {
        if (UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        return toAjax(postService.insertPost(post));
    }

    /**
     * 修改岗位
     */
    @GetMapping("/edit/{postId}")
    public String edit(@PathVariable("postId") Long postId, ModelMap mmap) {
        mmap.put("post", postService.selectPostById(postId));
        return prefix + "/edit";
    }

    /**
     * 修改保存岗位
     */
    @RequiresPermissions("system:post:edit")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated Post post) {
        if (UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return error("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return error("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        return toAjax(postService.updatePost(post));
    }

    /**
     * 校验岗位名称
     */
    @PostMapping("/checkPostNameUnique")
    @ResponseBody
    public String checkPostNameUnique(Post post) {
        return postService.checkPostNameUnique(post);
    }

    /**
     * 校验岗位编码
     */
    @PostMapping("/checkPostCodeUnique")
    @ResponseBody
    public String checkPostCodeUnique(Post post) {
        return postService.checkPostCodeUnique(post);
    }
}
