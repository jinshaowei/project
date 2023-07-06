package com.sky.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜品套餐表
 * */

@Mapper
public interface SetMealDishMapper {


    /**
    *    查询菜品套餐表
     *    根据菜品id查询菜品套餐表是否关联套餐id
    * */
    List<Long> countSetmealIdsByDishIds(List<Long> ids);
}
