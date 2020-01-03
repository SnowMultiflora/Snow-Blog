package com.zxw.service;

import com.zxw.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    /**
     * 新增
     * @param type
     * @return
     */
    Type saveType(Type type);

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    Type getType(Long id);

    /**
     * 查询所有分类
     * @param pageable
     * @return
     */
    Page<Type> listType(Pageable pageable);

    /**
     * 修改分类
     * @param id
     * @param type
     * @return
     */
    //Type updateType(Long id,Type type);

    /**
     * 删除分类
     * @param id
     */
    void deleteType(Long id);

    /**
     * 根据名字查分类
     * @param name
     * @return
     */
    Type getTypeByName(String name);

    List<Type> listType();

    List<Type> listTypesTop(Integer size);
}
