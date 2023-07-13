package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.UserSetMealService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/list")
    public Result<List> selectSetMealById(Long categoryId){
        log.info("根据分类id查询套餐：{}", categoryId);
        List SetMealList = userSetMealService.SetMealById(categoryId);
        return Result.success(SetMealList);
    }


}
