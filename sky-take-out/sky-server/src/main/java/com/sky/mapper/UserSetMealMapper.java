package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserSetMealMapper {

    //根据分类id查询套餐
    List<Setmeal> selectSetmealById(Long categoryId);

    /**
     * 根据套餐id查询包含的菜品
     *
     * @param id
     * @return
     */
    @Select("select sd.copies,d.description,d.image,sd.name from setmeal_dish sd ,dish d where sd.dish_id = d.id and sd.setmeal_id = #{id} and d.status = 1")
    List<DishVO> selectSetmealByIds(Long id);


}
