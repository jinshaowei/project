package com.sky.mapper;


import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜品套餐表
 */

@Mapper
public interface SetMealDishMapper {


    /**
     * 查询菜品套餐表
     * 根据菜品id查询菜品套餐表是否关联套餐id
     */
    List<Long> countSetmealIdsByDishIds(List<Long> ids);

    @Insert("insert into setmeal_dish (id, setmeal_id, dish_id, name, price, copies) VALUES " +
            "(#{id},#{setmealId},#{dishId},#{name},#{price},#{copies})")
    void insertSetmealDish(SetmealDish setmealDish);
}
