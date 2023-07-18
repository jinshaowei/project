package com.sky.service.appservice;

import com.sky.vo.DishVO;
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

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    List<DishVO> selectSetMealById(Long id);
}
