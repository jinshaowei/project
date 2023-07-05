package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DishService {

    /**
     * 新增菜品
     * */
    void inst(DishDTO dto);



    /**
     * 分页查询
     * */
    PageResult select(DishPageQueryDTO pageQueryDTO);

}
