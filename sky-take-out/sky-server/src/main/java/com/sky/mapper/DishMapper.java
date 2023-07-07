package com.sky.mapper;



import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {

    @AutoFill(OperationType.INSERT)
    //表示将主键返回，设置true表示要使用当前的主键，
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into dish (name, category_id, price, image, description, status, create_time, update_time, create_user, update_user)" +
            "VALUES (#{name},#{categoryId},#{price},#{image},#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser});")
    void inst(Dish dish);

    /*
    * 分页查询
    * */
    List<DishVO> select(DishPageQueryDTO pageQueryDTO);

    /*
    * 查询统计起售的菜品
    * */

    Long countDishByIds(List<Long> ids);

    /*
    * 根据id删除菜品信息
    * */
    void deleteDishIds(List<Long> ids);

    /*
    * 根据id查询菜品信息
    * */
    @Select("select * from dish where id = #{id}")
    DishVO selectId(Long id);

    /*
    * 修改菜品数据
    * */
//    @Update("update dish set name = #{name}, price = #{price},image = #{image},description = #{description} where id = #{id};")
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);
}
