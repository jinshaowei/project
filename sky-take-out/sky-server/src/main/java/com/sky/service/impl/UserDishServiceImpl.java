package com.sky.service.impl;


import com.sky.mapper.UserDishMapper;
import com.sky.service.UserDishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;


@Slf4j
@Service
public class UserDishServiceImpl implements UserDishService {

    @Autowired
    private UserDishMapper userDishMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List DishById(Long categoryId) {
        String redisDishKey = "dish:cache:" + categoryId;
        log.info("redis的key为: {}" , redisDishKey);

        List<DishVO> dishList = (List<DishVO>) redisTemplate.opsForValue().get(redisDishKey);
        //判断redis是否缓存了菜品
        if (!CollectionUtils.isEmpty(dishList)){
            log.info("查询缓存中的菜品...");
            //如果缓存中查询有菜品，则返回缓存
            return dishList;
        }

        //如果缓存没有菜品再查询数据库
         dishList = userDishMapper.DishById(categoryId);


        //再将数据库查询的菜品添加到redis缓存中
        redisTemplate.opsForValue().set(redisDishKey,dishList);
        log.info("查询数据库中的菜品...");
        return dishList;
    }

}
