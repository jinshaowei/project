package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    //口味表
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品
     * */
    @Transactional     //开启一个事务，当保存口味失败后进行回滚
    @Override
    public void inst(DishDTO dto) {
        //补全菜品基本属性
        Dish dish = Dish.builder()
                .id(dto.getId())
                .name(dto.getName())
                .categoryId(dto.getCategoryId())
                .price(dto.getPrice())
                .image(dto.getImage())
                .description(dto.getDescription())
                .status(StatusConstant.DISABLE)
                .build();

        //返回携带了菜品id
        dishMapper.inst(dish);

        //保存菜品口味信息
        List<DishFlavor> flavors = dto.getFlavors();
        flavors.forEach(dishFlavor1 ->{
            //mapper已经返回了dish保存的主键
            //对遍历的每一个菜品口味id赋值
            dishFlavor1.setDishId(dish.getId());
        });

        //再根据id插入到口味表
        dishFlavorMapper.insertBatch(flavors);

    }

    /**
     * 分页查询
     * */
    @Override
    public PageResult select(DishPageQueryDTO pageQueryDTO) {
        //获取总记录数和起始索引
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());

        //查询结果封装
        List<DishVO> list = dishMapper.select(pageQueryDTO);

        //转成page
        Page<DishVO> page = (Page<DishVO>) list;

        //返回结果
        return new PageResult(page.getTotal(), page.getResult());
    }

}
