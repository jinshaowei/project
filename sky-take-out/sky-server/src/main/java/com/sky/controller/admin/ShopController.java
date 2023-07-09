package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Api(tags = "店铺相关接口")
@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
public class ShopController {

    //将redis的键设置成常量
    public static final String KEY = "SHOP_STATUS";

    //注入Redis配置类的对象
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺状态
     * */
    @ApiOperation("设置店铺状态")
    @PutMapping("/{status}")
    public Result<Integer> setStatus(@PathVariable Integer status){
        redisTemplate.opsForValue().set(KEY,status);
        log.info("设置店铺状态为：{}", status == 0 ? "打烊中" : "营业中");
        return Result.success();
    }

    /**
     * 获取店铺状态
     * */
    @ApiOperation("获取店铺状态")
    @GetMapping("/status")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺状态为：{}", status == 0 ? "打烊中" : "营业中");
        return Result.success(status);
    }

}
