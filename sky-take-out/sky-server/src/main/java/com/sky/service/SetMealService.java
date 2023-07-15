package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 套餐起售-停售
     * @param status
     */
    void updateStatusById(Integer status, Long id);

    /**
     * 批量输出套餐
     * @param ids
     */
    void deleteByIds(List<Long>  ids);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO selectSetMalByIds(Long id);

    /**
     * 修改数据
     * @param setmealVO
     * @return
     */
    void updateSetMeal(SetmealVO setmealVO);
}
