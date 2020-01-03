package com.zxw.service;

import com.zxw.pojo.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagsService {
    Tags saveTag(Tags tags);

    Tags getTag(Long id);

    Tags getTagByName(String name);

    Page<Tags> listTag(Pageable pageable);

    List<Tags> listTag();

    List<Tags> listTagTop(Integer size);

    List<Tags> listTag(String ids);

  //  Tags updateTag(Long id, Tags tags);

    void deleteTag(Long id);
}
