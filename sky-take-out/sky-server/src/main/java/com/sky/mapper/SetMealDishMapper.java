package com.sky.mapper;


import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据id查询套餐回显
     * @param ids
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{ids};")
    List<SetmealDish> selectSetMealDish(Long ids);

    /**
     * 根据套餐id删除关联菜品
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteSetMealDishById(Long id);




    /**
     * 根据套餐id添加套餐关联菜品
     * @param setmealDishes
     * @return
     */
//    @Insert("insert into setmeal_dish (setmeal_id, dish_id, name, price, copies) VALUES (#{setmealId},#{dishId},#{name},#{price},#{copies});")
    void insertSetmealDishById(List<SetmealDish> setmealDishes);
}
