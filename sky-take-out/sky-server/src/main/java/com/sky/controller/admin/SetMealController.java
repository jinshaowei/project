package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @ApiOperation("套餐起售-停售")
    @PostMapping("/status/{status}")
    public Result updateSetMealStatus(@PathVariable Integer status, Long id){
        log.info("套餐起售-停售：{},套餐id为：{}", status, id);
        setMealService.updateStatusById(status,id);
        return Result.success();
    }

    @ApiOperation("批量删除套餐")
    @DeleteMapping
    public Result deleteByIds(@RequestParam("ids") List<Long> ids){
        log.info("删除菜品id：{}", ids);
        setMealService.deleteByIds(ids);
        return Result.success();
    }

    @ApiOperation("根据id查询套餐")
    @GetMapping("{id}")
    public Result<SetmealVO> selectSetMalById(@PathVariable Long id){
        log.info("根据id查询套餐：{}", id);
        SetmealVO setmealVO = setMealService.selectSetMalByIds(id);
        return Result.success(setmealVO);
    }

    @ApiOperation("修改套餐")
    @PutMapping
    public Result updateSetMeal(@RequestBody SetmealVO setmealVO){
        log.info("修改套餐：{}" ,setmealVO);
          setMealService.updateSetMeal(setmealVO);
        return Result.success();
    }

}
