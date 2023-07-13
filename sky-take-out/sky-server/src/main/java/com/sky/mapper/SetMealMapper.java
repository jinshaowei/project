package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface SetMealMapper {

    /**
     * 新增套餐
     *
     * @param setmeal
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(OperationType.INSERT)
    @Insert("insert into setmeal (id,category_id, name, price, status, description, image, create_time, update_time, create_user, update_user)" +
            " VALUES (#{id},#{categoryId}, #{name},#{price},#{status},#{description},#{image},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insertSetMeal(Setmeal setmeal);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    List<Setmeal> selectSetmealPage(SetmealPageQueryDTO setmealPageQueryDTO);
}
