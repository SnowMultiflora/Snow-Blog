package com.zxw.service.impl;

import com.zxw.dao.TagsDao;
import com.zxw.pojo.Tags;
import com.zxw.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagsServiceImpl implements TagsService {

    @Autowired
    private TagsDao tagsDao;

    /**
     * 新增  修改
     * @param tags
     * @return
     */
    @Override
    @Transactional
    public Tags saveTag(Tags tags) {
        return this.tagsDao.save(tags);
    }

    @Override
    @Transactional
    public Tags getTag(Long id) {
        return this.tagsDao.findById(id).get();
    }

    @Override
    public Tags getTagByName(String name) {
        return this.tagsDao.findByName(name);
    }

    @Override
    @Transactional
    public Page<Tags> listTag(Pageable pageable) {
        return this.tagsDao.findAll(pageable);
    }

    @Override
    public List<Tags> listTag() {
        return this.tagsDao.findAll();
    }

    @Override
    public List<Tags> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable= PageRequest.of(0, size, sort);
        return tagsDao.findTop(pageable);
    }

   //获取一组tags标签
    @Override
    public List<Tags> listTag(String ids) {
        return tagsDao.findAllById(convertToList(ids));
    }

    //将获取的一组tags标签的id转换成数组
    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
    /*@Override
    public Tags updateTag(Long id, Tags tags) {
        return null;
    }*/

    @Override
    @Transactional
    public void deleteTag(Long id) {
        this.tagsDao.deleteById(id);
    }
}
