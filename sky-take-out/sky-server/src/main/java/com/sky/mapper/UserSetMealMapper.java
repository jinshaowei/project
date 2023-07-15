package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserSetMealMapper {


    List<Setmeal> selectSetmealById(Setmeal setmeal);


    @Select("select sd.copies,d.description,d.image,sd.name from setmeal_dish sd ,dish d where sd.dish_id = d.id and sd.setmeal_id = #{id}")
    List<DishVO> selectSetmealByIds(Long id);
}
