package com.sky.mapper;

import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserSetMealMapper {


    List<Setmeal> selectSetmealById(Setmeal setmeal);
}