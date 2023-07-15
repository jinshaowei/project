package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserShoppingCartMapper {

    /**
     * 根据用户、菜品、口味、id查询
     *
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> selectShoppingCart(ShoppingCart shoppingCart);

    /**
     * 修改份数
     *
     * @param cart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateShoppingCart(ShoppingCart cart);

    /**
     * 添加购物车
     *
     * @param cart
     */
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time)" +
            " VALUES (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{amount},#{createTime})")
    void insert(ShoppingCart cart);

    @Select("select id, name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> selectUserById(Long userId);

    /**
     * 根据id删除购物车的菜品信息
     * @param shoppingCartDTO
     */
    void deleteById(ShoppingCartDTO shoppingCartDTO);

    /**
     * 根据菜品或套餐id查询购物车
     * @param shoppingCartDTO
     * @return
     */
    ShoppingCart selectById(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void delete(Long userId);
}
