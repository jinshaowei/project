package com.sky.mapper.appmapper;

import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserCategoryMapper {

    /**
     * 查询分类
     * @param category
     * @return
     */
    List<Category> inquireById(Category category);

}
