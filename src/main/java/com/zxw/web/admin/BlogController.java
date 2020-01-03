package com.zxw.web.admin;


import com.zxw.pojo.Blog;
import com.zxw.pojo.User;
import com.zxw.service.BlogService;
import com.zxw.service.TagsService;
import com.zxw.service.TypeService;
import com.zxw.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 后台：博客控制层
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagsService tagsService;

    /**
     * 博客页面  带分页查询
     * @param pageable
     * @param blogQuery
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blogQuery, Model model){
        //分类下拉列表查询
        model.addAttribute("types",typeService.listType());

        model.addAttribute("page",blogService.listBlog(pageable, blogQuery));
        return "admin/blogs";
    }

    /**
     * 博客表格局部刷新
     * @param pageable
     * @param blogQuery
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blogQuery, Model model){
        model.addAttribute("page",blogService.listBlog(pageable, blogQuery));
        return "admin/blogs :: blogList";
    }

    /**
     * 增加跳转
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTags(model);
        model.addAttribute("blog",new Blog());
        return "admin/blogs-input";
    }

    /**
     * 添加跳转和修改跳转的共同部分
     * @param model
     */
    private void setTypeAndTags(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagsService.listTag());
    }

    /**
     * 修改跳转
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        setTypeAndTags(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return "admin/blogs-input";
    }

    /**
     * 添加和修改博客
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        //获取用户
        blog.setUser((User) session.getAttribute("user"));
        //获取分类
        blog.setType(this.typeService.getType(blog.getType().getId()));
        //获取标签
        blog.setTags(this.tagsService.listTag(blog.getTagIds()));
        //新增博客
        Blog b;
        if (blog.getId() == null) {
            b =  blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }
        if (b == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/blogs";
    }

    /**
     * 删除博客
     * @param id
     * @return
     */
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        this.blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";
    }
}
