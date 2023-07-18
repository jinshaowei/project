package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.appservice.UserSetMealService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("UserSetMealController")
@Slf4j
@Api(tags = "用户端套餐相关接口")
@RequestMapping("user/setmeal")
public class SetMealController {

    @Autowired
    private UserSetMealService userSetMealService;

    @CachePut(cacheNames = "setmeal_cache", key = "#a0")
    @ApiOperation("根据分类id查询套餐")
    @GetMapping("/list")
    public Result<List> selectSetMealById(Long categoryId){
        log.info("根据分类id查询套餐：{}", categoryId);
        List SetMealList = userSetMealService.SetMealById(categoryId);
        return Result.success(SetMealList);
    }

    @CachePut(cacheNames = "setmeal_cache", key = "#a0")
    @ApiOperation("根据套餐id查询包含的菜品")
    @Qualifier
    @GetMapping("dish/{id}")
    public Result<List> setmealVOResultById(@PathVariable Long id){
        log.info("根据套餐id查询包含的菜品：{}", id);
        List<DishVO> dishVO = userSetMealService.selectSetMealById(id);
        return Result.success(dishVO);
    }


}
