package com.zxw.service.impl;

import com.zxw.NotFoundException;
import com.zxw.dao.TypeDao;
import com.zxw.pojo.Type;
import com.zxw.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeDao typeDao;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return this.typeDao.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return this.typeDao.findById(id).get();
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return this.typeDao.findAll(pageable);
    }

   /* @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t=typeDao.findById(id).get();
        if (t==null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type,t);
        return this.typeDao.save(t);
    }*/

    @Transactional
    @Override
    public void deleteType(Long id) {
        this.typeDao.deleteById(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return this.typeDao.findByName(name);
    }

    @Override
    public List<Type> listType() {
        return this.typeDao.findAll();
    }

    @Override
    public List<Type> listTypesTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable= PageRequest.of(0, size, sort);
        return typeDao.findTop(pageable);
    }
}
