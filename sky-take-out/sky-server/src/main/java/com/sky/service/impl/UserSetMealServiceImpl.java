package com.sky.service.impl;

import com.sky.entity.Setmeal;
import com.sky.mapper.UserSetMealMapper;
import com.sky.service.UserSetMealService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class UserSetMealServiceImpl implements UserSetMealService {

    @Autowired
    private UserSetMealMapper userSetMealMapper;

    //用redis缓存菜品
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @Override
    public List SetMealById(Long categoryId) {
        String redisSetMealKey = "setmeal:" + categoryId;

        //判断缓存中有没有套餐信息
        List<Setmeal> setMealList = (List<Setmeal>) redisTemplate.opsForValue().get(redisSetMealKey);
        if (!CollectionUtils.isEmpty(setMealList)){
            log.info("查询缓存中的套餐的信息...");
            return setMealList;
        }

        //没有则查询缓存套餐
        setMealList =  userSetMealMapper.selectSetmealById(categoryId);

        //将数据添加到数据库中
        redisTemplate.opsForValue().set(redisSetMealKey,setMealList);
        log.info("查询数据库中的套餐的信息...");
        return setMealList;
    }

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @Override
    public List<DishVO> selectSetMealById(Long id) {
        //设置redis的key
        String setmealKey = "setmeal:cache:" + id;

        //根据key获取套餐列表
        List<DishVO> setmealDishes = (List<DishVO>) redisTemplate.opsForValue().get(setmealKey);

        //判断缓存是否有套餐
        if (!CollectionUtils.isEmpty(setmealDishes)){
            log.info("查询缓存中的套餐包含菜品的信息...");
            return setmealDishes;
        }
        //没有就查询数据库套餐信息
         setmealDishes = userSetMealMapper.selectSetmealByIds(id);
        //将套餐信息添加到缓存
        redisTemplate.opsForValue().set(setmealKey,setmealDishes);
        log.info("查询数据库中的套餐包含菜品的信息...");
        return setmealDishes;
    }
}
