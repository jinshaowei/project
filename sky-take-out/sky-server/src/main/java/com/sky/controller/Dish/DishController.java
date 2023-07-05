package com.sky.controller.Dish;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "菜品管理")
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @ApiOperation("新增菜品")
    @PostMapping
    public Result insert(@RequestBody DishDTO dto){
        log.info("新增菜品：{}", dto);
        dishService.inst(dto);
        return Result.success();
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> select(DishPageQueryDTO pageQueryDTO){
        log.info("查询每页菜品：{}", pageQueryDTO);
        PageResult pageResult = dishService.select(pageQueryDTO);
        return Result.success(pageResult);
    }


}
