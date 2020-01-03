package com.zxw.service.impl;

import com.zxw.NotFoundException;
import com.zxw.dao.BlogDao;
import com.zxw.pojo.Blog;
import com.zxw.pojo.Type;
import com.zxw.service.BlogService;
import com.zxw.utils.MarkdownUtils;
import com.zxw.utils.MyBeanUtils;
import com.zxw.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * 博客业务层
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    /**
     * 根据id获得博客
     * @param id
     * @return
     */
    @Override
    public Blog getBlog(Long id) {
        return this.blogDao.findById(id).get();
    }

    /**
     * 前台首页博客查询
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return this.blogDao.findAll(pageable);
    }

    /**
     * 标签页面的博客查询
     * @param tagId
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return this.blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    /**
     * 前台首页推荐部分查询
     * @param size
     * @return
     */
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable= PageRequest.of(0, size, sort);
        return blogDao.findTop(pageable);
    }

    /**
     * 前台搜索框查询
     * @param query
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return this.blogDao.findByQuery(query, pageable);
    }

    /**
     *后台功能：添加博客
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId()==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else {
            blog.setUpdateTime(new Date());
        }
        return this.blogDao.save(blog);
    }

    /**
     * 后台功能：条件查询
     * @param pageable
     * @param blogQuery
     * @return
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {
        return this.blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //组合条件
                List<Predicate> predicates=new ArrayList<>();
                if (!"".equals(blogQuery.getTitle()) && blogQuery.getTitle()!=null){
                    predicates.add(criteriaBuilder.like(root.<String>get("title"),"%"+blogQuery.getTitle()));
                }
                if (blogQuery.getTypeId() != null){
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"),blogQuery.getTypeId()));
                }
                //是否推荐
                if (blogQuery.isRecommend()){
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"),blogQuery.isRecommend()));
                }

                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    /**
     * 后台功能：修改博客
     * @param id
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogDao.findById(id).get();
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogDao.save(b);
    }

    /**
     * 后台功能：删除博客
     * @param id
     */
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        this.blogDao.deleteById(id);
    }

    /**
     * 前台功能：归档
     * Map的key为年份，value是一个博客集合
     * @return
     */
    @Override
    public Map<String, List<Blog>> archivesBlog() {
        List<String> years = this.blogDao.findGroupYear();
        Map<String,List<Blog>> map=new HashMap<>();
        for (String year:years) {
            System.out.println(year);
            map.put(year,this.blogDao.findByYear(year));
        }
        return map;
    }


    /**
     * 归档文章数
     * @return
     */
    @Override
    public Long countBlog() {
        return this.blogDao.count();
    }

    /**
     * 前台功能：博客详情展示
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = this.blogDao.findById(id).get();
        if (blog==null){
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        //文章格式处理
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        this.blogDao.updateViews(id);
        return b;
    }
}
