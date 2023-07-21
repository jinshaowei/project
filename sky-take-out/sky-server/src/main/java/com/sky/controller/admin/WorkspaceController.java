package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.adminservice.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/workspace")
@Api(tags = "工作台接口")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @ApiOperation("查询今日运营数据")
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData(){
        log.info("查询今日运营数据");
        BusinessDataVO businessDataVO = workspaceService.selectOperationalData();
        return Result.success(businessDataVO);
    }

    @ApiOperation("查询套餐总览")
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        log.info("查询套餐");
        SetmealOverViewVO setmeal =workspaceService.selectSetmeals();
        return Result.success(setmeal);
    }


    @ApiOperation("查询菜品总览")
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> overviewDishes(){
        log.info("查询菜品总览");
        DishOverViewVO dishOverViewVO = workspaceService.countDishStatus();
        return Result.success(dishOverViewVO);
    }


}
