package com.sky.controller.Dish;

import com.sky.dto.DishDTO;
import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Api(tags = "菜品管理")
@RestController
@RequestMapping("/admin")
public class DishController {

    @Autowired
    private DishService dishService;




    @ApiOperation("新增菜品")
    @PostMapping("/dish")
    public Result insert(@RequestBody DishDTO dto){
        log.info("新增菜品：{}", dto);
        dishService.inst(dto);
        return Result.success();
    }

}
