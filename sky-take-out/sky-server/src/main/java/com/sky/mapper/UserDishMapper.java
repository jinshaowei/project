package com.sky.mapper;

import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;


import java.util.List;

@Mapper
public interface UserDishMapper {


    /**
     * 根据分类id查询菜品
     * @param dishVO
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    List<DishVO> DishById(DishVO dishVO);
}
