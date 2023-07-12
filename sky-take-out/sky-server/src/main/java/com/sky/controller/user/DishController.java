package com.sky.controller.user;


import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.UserDishService;
import com.sky.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("UserDishController")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {

    @Autowired
    private UserDishService userDishService;

    @ApiOperation("根据分类id查询菜品")
    @GetMapping("user/dish/list")
    public Result<List> selectGetId(Long categoryId){
        log.info("分类id：{}", categoryId);
        List list =  userDishService.DishById(categoryId);
        return Result.success(list);
    }
}
