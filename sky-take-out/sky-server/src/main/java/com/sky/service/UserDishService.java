package com.sky.service;

import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserDishService {

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List DishById(Long categoryId);
}
