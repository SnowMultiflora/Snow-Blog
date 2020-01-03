package com.zxw.web;

import com.zxw.pojo.Tags;
import com.zxw.pojo.Type;
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

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagsService tagsService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long id, Model model){
        List<Tags> tags = this.tagsService.listTagTop(10000);

        //点击导航的分类时，默认第一个分类
        if (id==-1){
            id=tags.get(0).getId();
        }

        model.addAttribute("tags",tags);
        model.addAttribute("page",this.blogService.listBlog(id,pageable));
        model.addAttribute("activeTagId",id);
        return "tags";
    }


}
