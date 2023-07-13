package com.sky.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserSetMealService {


    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    List SetMealById(Long categoryId);
}
