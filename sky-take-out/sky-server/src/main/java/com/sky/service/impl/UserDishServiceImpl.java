package com.sky.service.impl;


import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.UserDishMapper;
import com.sky.service.UserDishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class UserDishServiceImpl implements UserDishService {

    @Autowired
    private UserDishMapper userDishMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    /**
     * 根据分类id查询菜品
     * @param dish
     * @return
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        String redisDishKey = "dish:cache:" + dish.getCategoryId();
        log.info("redis的key为: {}" , redisDishKey);

        List<DishVO> dishVOList = (List<DishVO>) redisTemplate.opsForValue().get(redisDishKey);
        //判断redis是否缓存了菜品
        if (!CollectionUtils.isEmpty(dishVOList)){
            log.info("查询缓存中的菜品...");
            //如果缓存中查询有菜品，则返回缓存
            return dishVOList;
        }

        //如果缓存没有菜品再查询数据库
        List<Dish> dishList = userDishMapper.DishById(dish);
        dishVOList = new ArrayList<>();
        //TODO 导入代码
        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.select(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        //再将数据库查询的菜品添加到redis缓存中
        redisTemplate.opsForValue().set(redisDishKey,dishVOList);
        log.info("查询数据库中的菜品...");
        return dishVOList;
    }

}
