package com.sky.mapper;

import com.sky.entity.Dish;
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
     * @param dish
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Select("select id, name, category_id, price, image, description, status, create_time, update_time, create_user,update_user from dish where status = 1 and category_id = #{categoryId}")
    List<Dish> DishById(Dish dish);
}
