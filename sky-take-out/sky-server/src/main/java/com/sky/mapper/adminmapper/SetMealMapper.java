package com.sky.mapper.adminmapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.*;

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
     *
     * @param setmealPageQueryDTO
     * @return
     */
    List<SetmealVO> selectSetmealPage(SetmealPageQueryDTO setmealPageQueryDTO);

    @Update("update setmeal set status = #{status} where id = #{id}; ")
    void updateStatusByIds(Integer status, Long id);

    /**
     * 查询起售中的套餐
     *
     * @param ids
     * @return
     */
    Long selectByIds(List<Long> ids);


    /**
     * 根据id批量删除套餐
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);


    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Select("select * from setmeal where id = #{id}")
    SetmealVO selectById(Long id);

    /**
     * 修改套餐
     * @param setmeal
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(OperationType.UPDATE)
    void updateSetMeal(Setmeal setmeal);

    /*
    * 修改套餐状态
    * */
    void updateSetMealByids(List<Long> setMealById);

    /**
     * 查询套餐停售数量
     * @param unPaid
     * @return
     */
    @Select("select count(id) from setmeal where status = #{unPaid};")
    Integer selectStatus(Integer unPaid);
}
