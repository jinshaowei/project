package com.sky.controller.classification;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("根据ID删除员工")
    @DeleteMapping()
    public Result delete(Integer id){
        log.info("删除员工：{}", id);
        categoryService.delete(id);
        return Result.success();
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO category){
        log.info("分页查询：{}", category);
        PageResult pageResult = categoryService.page(category);
        return Result.success(pageResult);
    }




}
