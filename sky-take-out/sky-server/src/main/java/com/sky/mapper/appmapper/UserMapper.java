package com.sky.mapper.appmapper;

import com.sky.entity.Category;
import com.sky.entity.User;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 查询openid
     * @param openid
     * @return
     */
    @Select("select id, openid, name, phone, sex, id_number, avatar, create_time from user where openid = #{openid};")
    User selectById(String openid);

    /**
     * 添加用户
     * @param user
     */
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into user (openid, name, phone, sex, id_number, avatar, create_time) VALUES " +
            "(#{openid},#{name},#{phone},#{sex},#{idNumber},#{avatar},#{createTime})")
    void insert(User user);


    /**
     * 查询分类
     * @param category
     * @return
     */
    List<Category> inquireById(Category category);



    /**
     * 根据分类id查询菜品
     * @param dishVO
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    List<DishVO> DishById(DishVO dishVO);

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @Select("select id, openid, name, phone, sex, id_number, avatar, create_time from user where id = #{userId}")
    User getById(Long userId);
}
