package com.sky.service.impl;

import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.UserDishMapper;
import com.sky.service.UserDishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDishServiceImpl implements UserDishService {

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private UserDishMapper userDishMapper;

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List DishById(Long categoryId) {
        //将id封装到实体类
        DishVO dishVO = new DishVO();
        dishVO.setCategoryId(categoryId);
        //查询返回的菜品
        List<DishVO> dishList = userDishMapper.DishById(dishVO);

        //遍历list集合的菜品信息
        for (DishVO vo : dishList) {
            //根据菜品id查询口味信息
            Long id = vo.getId();
            vo.setFlavors(dishFlavorMapper.selectDishById(id));
        }
        return dishList;
    }

}
