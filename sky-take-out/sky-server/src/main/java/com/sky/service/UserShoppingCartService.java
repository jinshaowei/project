package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserShoppingCartService {
    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询购物车
     * @return
     */
    List<ShoppingCart> seclectUserId();

    /**
     * 删除购物车菜品
     * @param shoppingCartDTO
     */
    void deleteById(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void delete();
}
