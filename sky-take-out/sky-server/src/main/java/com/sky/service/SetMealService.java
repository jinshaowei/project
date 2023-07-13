package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

@Service
public interface SetMealService {


    /**
     * 新增套餐
     * @param setmealDTO
     */
    void insertSetMeal(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult selectPageSetmeal(SetmealPageQueryDTO setmealPageQueryDTO);
}
