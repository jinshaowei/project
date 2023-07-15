package com.sky.service.impl;

import com.sky.entity.Setmeal;
import com.sky.mapper.UserSetMealMapper;
import com.sky.service.UserSetMealService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;


import java.util.List;

@Slf4j
@Service
public class UserSetMealServiceImpl implements UserSetMealService {

    @Autowired
    private UserSetMealMapper userSetMealMapper;



    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */

    @Override
    public List SetMealById(Long categoryId) {
        List<Setmeal> setMealList =  userSetMealMapper.selectSetmealById(categoryId);

        log.info("查询数据库套餐...");
        return setMealList;

    }

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */

    @Override
    public List<DishVO> selectSetMealById(Long id) {
        List<DishVO> setmealDishes = userSetMealMapper.selectSetmealByIds(id);
        log.info("查询数据库套餐...");
        return setmealDishes;
    }
}
