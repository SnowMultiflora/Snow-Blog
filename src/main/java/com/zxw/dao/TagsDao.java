package com.zxw.dao;

import com.zxw.pojo.Tags;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagsDao extends JpaRepository<Tags,Long> {
    Tags findByName(String name);

    @Query("select t from Tags t")
    List<Tags> findTop(Pageable pageable);
}
