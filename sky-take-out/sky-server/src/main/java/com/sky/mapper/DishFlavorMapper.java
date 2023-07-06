package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {


    void insertBatch(List<DishFlavor> flavors);


    /*
    * 批量删除菜品口味信息
    * */
    void deleteByIds(List<Long> ids);
}
