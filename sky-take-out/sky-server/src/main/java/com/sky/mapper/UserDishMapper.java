package com.sky.mapper;

import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface UserDishMapper {


    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Select("select id, name, category_id, price, image, description, status, create_time, update_time, create_user,update_user from dish where category_id = #{categoryId}")
    List<DishVO> DishById(Long categoryId);
}
