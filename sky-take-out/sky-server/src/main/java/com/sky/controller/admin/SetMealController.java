package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "套餐相关接口")
@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @ApiOperation("新增套餐")
    @PostMapping
    public Result InsertSetMeal(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}", setmealDTO);
        setMealService.insertSetMeal(setmealDTO);
        return Result.success();
    }

    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult> inquirePageSetMeal(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询参数：{}",setmealPageQueryDTO);
        PageResult pageResult = setMealService.selectPageSetmeal(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

}
