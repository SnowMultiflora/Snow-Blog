package com.zxw.web;



import com.zxw.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ArchivesShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model){

        model.addAttribute("archivesMap",this.blogService.archivesBlog());
        model.addAttribute("blogCount",this.blogService.countBlog());
        return "archives";
    }
}
