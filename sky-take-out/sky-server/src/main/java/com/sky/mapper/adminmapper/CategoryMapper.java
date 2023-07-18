package com.sky.mapper.adminmapper;


import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
    * 新增分类
    * */
/*    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user)" +
            "VALUES (#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser}) ")*/

     //切面类注解
    @AutoFill(value = OperationType.INSERT)
    void inst(Category category);


    /**
    *分页查询
    * */
    List<Category> select(CategoryPageQueryDTO category);


    /**
     * 删除分类
     * */
    @Delete("delete from category where id = #{id}")
    void delete(Integer id);

    /**
     * 启用 - 禁用分类
     * */
    @AutoFill(OperationType.UPDATE)
    @Update("update category set status = #{status} where id = #{id};")
    void status(Category category);

    /**
     * 修改分类
     * */
    //切面类注解
    @AutoFill(OperationType.UPDATE)
    @Update("update category set name = #{name}, sort = #{sort}, type = #{type} where id = #{id};")
    void update(Category category);


    /**
     * 根据id查询菜品分类
     * */
    @Select("select * from category where type = #{type};")
    List<Category> selectId(Category category);

    /**
     * 根据菜品id修改分类名称
     * */
    @Update("update category set name = #{categoryName} where name = #{DishId};")
    void updateDishId(String categoryName,Long DishId);

    /**
     * 根据分类名称修改
     * */

    void updateName(String categoryByName, String categoryName);
}
