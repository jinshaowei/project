package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.UserShoppingCartMapper;
import com.sky.service.UserShoppingCartService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserShoppingCartServiceImpl implements UserShoppingCartService {

    @Autowired
    private UserShoppingCartMapper userShoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Transactional
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
       //将id拷贝到实体类中
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //获取当前用户id
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //先查询购物车是否有菜品或套餐
        List<ShoppingCart> list = userShoppingCartMapper.selectShoppingCart(shoppingCart);

        //判断 如果有则添加菜品份数
        if (!CollectionUtils.isEmpty(list)){
            //如果有菜品或套餐，只会有一份
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            userShoppingCartMapper.updateShoppingCart(cart);
        }else {
            //集合不为空则判断菜品是否为空
            if (shoppingCart.getDishId() != null){  // 不为空则添加菜品信息
                DishVO dishVO = dishMapper.selectId(shoppingCart.getDishId());
                shoppingCart.setAmount(dishVO.getPrice());
                shoppingCart.setImage(dishVO.getImage());
                shoppingCart.setName(dishVO.getName());
            }else {
                //为空则表示添加的是套餐id
                SetmealVO setmealVO = setMealMapper.selectById(shoppingCart.getSetmealId());
                shoppingCart.setAmount(setmealVO.getPrice());
                shoppingCart.setImage(setmealVO.getImage());
                shoppingCart.setName(setmealVO.getName());
            }
            //修改购物车时间
            shoppingCart.setCreateTime(LocalDateTime.now());
            //购物车有菜品，份数默认是1
            shoppingCart.setNumber(1);

            //添加
            userShoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查询购物车
     * @return
     */
    @Override
    public List<ShoppingCart> seclectUserId() {
        Long userId = BaseContext.getCurrentId();
        //根据用户id查询当前购物车
        List<ShoppingCart> list = userShoppingCartMapper.selectUserById(userId);
        return list;
    }

    /**
     * 删除购物车中的一个菜品
     */
    @Transactional
    @Override
    public void deleteById(ShoppingCartDTO shoppingCartDTO) {
        //查询出当前购物车中菜品的数量
        ShoppingCart cartList = userShoppingCartMapper.selectById(shoppingCartDTO);

        //判断数量是否大于1，大于1则-1
        if (cartList.getNumber() >= 1){
            cartList.setNumber(cartList.getNumber() - 1);
            userShoppingCartMapper.updateShoppingCart(cartList);
            if (cartList.getNumber() == 0){
                // 小于则删除
                userShoppingCartMapper.deleteById(shoppingCartDTO);
            }
        }
    }

    /**
     * 清空购物车
     */
    @Override
    public void delete() {
        Long userId = BaseContext.getCurrentId();
        userShoppingCartMapper.delete(userId);
    }

}
