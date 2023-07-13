package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@Slf4j
public class SetMealServiceImpl implements SetMealService{

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    public void insertSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();

        setmeal.setId(setmealDTO.getId());
        setmeal.setCategoryId(setmealDTO.getCategoryId());
        setmeal.setName(setmealDTO.getName());
        setmeal.setPrice(setmealDTO.getPrice());
        setmeal.setStatus(setmealDTO.getStatus());
        setmeal.setDescription(setmealDTO.getDescription());
        setmeal.setImage(setmealDTO.getImage());

        log.info("查询套餐：{}", setmeal);
        //添加套餐
       setMealMapper.insertSetMeal(setmeal);

       //添加套餐和菜品关系表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
            setMealDishMapper.insertSetmealDish(setmealDish);
        });


    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult selectPageSetmeal(SetmealPageQueryDTO setmealPageQueryDTO) {
        //获取总记录数，和当前页码
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        //将结果存储到list中
        List<Setmeal> setmealList = setMealMapper.selectSetmealPage(setmealPageQueryDTO);
        System.out.println(setmealList);

        //封装查询结果
        Page<Setmeal> page = (Page<Setmeal>) setmealList;

        return new PageResult(page.getTotal(),page.getResult());
    }

}
