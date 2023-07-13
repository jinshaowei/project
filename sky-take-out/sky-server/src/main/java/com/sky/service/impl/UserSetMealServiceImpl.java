package com.sky.service.impl;

import com.sky.entity.Setmeal;
import com.sky.mapper.UserSetMealMapper;
import com.sky.service.UserSetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(1);
        List<Setmeal> setMealList =  userSetMealMapper.selectSetmealById(setmeal);
        return setMealList;
    }
}
