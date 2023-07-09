package com.sky.controller.admin;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "分类管理")
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation("新增分类")
    @PostMapping
    public Result inst(@RequestBody CategoryDTO categoryDTO){
        log.info("新增信息：{}", categoryDTO);
       categoryService.inst(categoryDTO);
       return Result.success();
    }

    @ApiOperation("删除分类")
    @DeleteMapping()
    public Result delete(Integer id){
        log.info("删除分类：{}", id);
        categoryService.delete(id);
        return Result.success();
    }

    @ApiOperation("启用-禁用分类")
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status, Long id){
        log.info("启用-禁用：{}", status, id);
        categoryService.status(status, id);
       return Result.success();
    }


    @ApiOperation("修改分类")
    @PutMapping()
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类：{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();

    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO category){
        log.info("分页查询：{}", category);
        PageResult pageResult = categoryService.page(category);
        return Result.success(pageResult);


    }

    @ApiOperation("分类id查询")
    @GetMapping("list")
    public Result<List<Category>> select(Integer type){
        log.info("根据分类id查询：{}", type);
        List<Category> category = categoryService.select(type);
        return Result.success(category);
    }

}
