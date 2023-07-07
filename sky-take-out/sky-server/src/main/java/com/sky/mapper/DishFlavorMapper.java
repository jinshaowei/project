package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishFlavorMapper {


    /*
    * 添加菜品信息
    * */
    void insertBatch(List<DishFlavor> flavors);


    /*
    * 批量删除菜品口味信息
    * */
    void deleteByIds(List<Long> ids);

    /*
    * 根据id查询口味表
    * */
    @Select("select * from dish_flavor df where df.dish_id = #{id};")
    List<DishFlavor> select(Long id);



}
