package com.sky.mapper;

import com.sky.entity.Category;
import com.sky.entity.User;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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
