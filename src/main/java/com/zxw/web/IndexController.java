package com.zxw.web;

import com.zxw.service.BlogService;
import com.zxw.service.TagsService;
import com.zxw.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 前台首页控制层
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagsService tagsService;

    /**
     * 前台首页
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page",this.blogService.listBlog(pageable));
        model.addAttribute("types",this.typeService.listTypesTop(6));
        model.addAttribute("tags",this.tagsService.listTagTop(10));
        model.addAttribute("recommendBlogs", this.blogService.listRecommendBlogTop(8));
        return "index";
    }

    /**
     * 前台博客详情
     * @return
     */
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model Model){
        Model.addAttribute("blog",this.blogService.getAndConvert(id));
        return "blog";
    }

    /**
     * 前台搜索框
     * @param pageable
     * @param model
     * @return
     */
    @PostMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, @RequestParam String query, Model model){
        model.addAttribute("page",this.blogService.listBlog("%"+query+"%", pageable));
        model.addAttribute("query",query);
        return "search";
    }

    /**
     * footer最新博客
     */
    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogs",this.blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}
