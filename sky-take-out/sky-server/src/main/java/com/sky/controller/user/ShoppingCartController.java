package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.UserShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户端订单接口")
@RestController
@Slf4j
@RequestMapping("user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private UserShoppingCartService userShoppingCartService;

    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加的菜品：{}", shoppingCartDTO);
        userShoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    @ApiOperation("查看购物车")
    @GetMapping("/list")
    public Result<List> selectShopping(){
        log.info("查询购物车中菜品信息..");
        List<ShoppingCart> shoppingCartList = userShoppingCartService.seclectUserId();
        return Result.success(shoppingCartList);
    }


    @ApiOperation("根据id删除购物车菜品")
    @PostMapping("/sub")
    public Result deleteById(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车中菜品信息id：{}", shoppingCartDTO);
        userShoppingCartService.deleteById(shoppingCartDTO);
        return Result.success();
    }

    @ApiOperation("批量删除购物车菜品")
    @DeleteMapping("/clean")
    public Result delete(){
        log.info("清空购物车...");
        userShoppingCartService.delete();
        return Result.success();
    }

}
