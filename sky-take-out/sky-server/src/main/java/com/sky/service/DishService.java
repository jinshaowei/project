package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DishService {

    /**
     * 新增菜品
     * */
    void inst(DishDTO dto);


}
