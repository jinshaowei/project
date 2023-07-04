package com.sky.mapper;



import com.sky.entity.Category;
import com.sky.entity.Dish;

import org.apache.ibatis.annotations.*;

@Mapper
public interface DishMapper {

//    @AutoFill   Aop填充公共字段
    //表示将主键返回，设置true表示要使用当前的主键，
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into dish (name, category_id, price, image, description, create_time, update_time, create_user, update_user)" +
            "VALUES (#{name},#{categoryId},#{price},#{image},#{description},#{createTime},#{updateTime},#{createUser},#{updateUser});")
    void inst(Dish dish);

}
