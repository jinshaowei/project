package com.sky.mapper;


import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
    * 新增分类
    * */
/*    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user)" +
            "VALUES (#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser}) ")*/
    void inst(Category category);


    /*
    *
    * */
    List<Category> select(CategoryPageQueryDTO category);


    /**
     * 删除分类
     * */
    @Delete("delete from category where id = #{id}")
    void delete(Integer id);


    @Update("update category set status = #{status} where id = #{id};")
    void status(Category category);
}
