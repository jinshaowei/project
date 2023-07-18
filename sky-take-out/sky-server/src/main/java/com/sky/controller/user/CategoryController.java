package com.sky.controller.user;


import com.sky.result.Result;
import com.sky.service.appservice.UserCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "C端分类接口")
@RestController("UserCategoryController")
@Slf4j
public class CategoryController {

    @Autowired
    private UserCategoryService userCategoryService;

    @ApiOperation("根据套餐类型查询分类列表")
    @GetMapping("/user/category/list")
    public Result<List> selectById(Integer type){
        log.info("根据type为：{} 查询分类", type);
       List list = userCategoryService.selectById(type);
        return Result.success(list);
    }


}
