package com.sky.controller.Dish;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @ApiOperation("删除菜品")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除菜品：{}",ids);
        dishService.delete(ids);
        return Result.success();
    }

    @ApiOperation("根据id回显数据")
    @GetMapping("/{id}")
    public Result<DishVO> dishVOResultById(@PathVariable Long id){
        log.info("根据id查询数据回显：{}",id);
        DishVO dishVO =  dishService.selectId(id);
        return Result.success(dishVO);
    }

    @ApiOperation("修改数据")
    @PutMapping
    public Result update(@RequestBody DishVO dishVO){
        log.info("修改菜品数据：{}",dishVO);
        dishService.update(dishVO);
        return Result.success();
    }

}
