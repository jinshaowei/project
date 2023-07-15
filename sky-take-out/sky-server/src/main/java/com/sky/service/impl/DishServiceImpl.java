package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.BaseException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class DishServiceImpl implements DishService {

    //菜品表
    @Autowired
    private DishMapper dishMapper;

    //口味表
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    //菜品套餐关系表
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    //redis缓存
    @Autowired
    private RedisTemplate redisTemplate;

    //套餐表
    @Autowired
    private SetMealMapper setMealMapper;


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
        if (flavors.size() != 0) {
            flavors.forEach(dishFlavor1 -> {
                //mapper已经返回了dish保存的主键
                //对遍历的每一个菜品口味id赋值
                dishFlavor1.setDishId(dish.getId());
            });

            //再根据id插入到口味表
            dishFlavorMapper.insertBatch(flavors);
        }

        //清除菜品缓存
        eliminateCache("*");

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


    /**
     * 删除菜品
     * */
    @Transactional     //出现运行时异常进行回滚事务
    @Override
    public void delete(List<Long> ids) {
        // 判断菜品是否是起售
        //查询统计起售菜品的数量
        Long count = dishMapper.countDishByIds(ids);
        //大于0表示有起售菜品
        if (count > 0){

            throw new BaseException(MessageConstant.DISH_ON_SALE);
        }

        //判断菜品是否关联套餐
        //查询菜品套餐表的菜品id是否有关联套餐的id
        List<Long> countIds =  setMealDishMapper.countSetmealIdsByDishIds(ids);
        //判断集合是否为空， 有则不为空
        if (!CollectionUtils.isEmpty(countIds)){
            throw new BaseException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品 及 口味信息
        dishMapper.deleteDishIds(ids);

        dishFlavorMapper.deleteByIds(ids);
        //清除菜品缓存
        eliminateCache("*");
    }

    /**
    * 查询数据回显
    * */
    @Transactional
    @Override
    public DishVO selectId(Long id) {

        //根据id查询数据
        DishVO voList = dishMapper.selectId(id);
        //根据菜品id查询口味信息
        List<DishFlavor> list = dishFlavorMapper.select(id);
        //口味信息存入实体类中
        voList.setFlavors(list);
        return voList;
    }

    /**
     * 修改菜品数据
     * */
    @Transactional
    @Override
    public void update(DishVO dishVO) {
        //修改的菜品信息
        Dish dish = new Dish();
        //拷贝对象
        BeanUtils.copyProperties(dishVO,dish);

        //修改菜品基本信息
        dishMapper.update(dish);

        //先删除口味信息，再添加
        dishFlavorMapper.deleteByIds(Collections.singletonList(dish.getId()));

        //判断是否有修改的菜品口味
        if (dishVO.getFlavors().size() != 0){
            //遍历口味列表，添加菜品id，再通过id进行添加口味信息
            List<DishFlavor> flavors = dishVO.getFlavors();
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            //添加口味信息
            dishFlavorMapper.insertBatch(flavors);
        }

        //清除菜品缓存
        eliminateCache("*");
    }

    /**
     * 根据分类id查询菜品
     * */
    @Override
    public List selectCategoryDsihByIds(Long categoryId, String name) {
        List<Dish> list =  dishMapper.selectCategoryDishByIds(categoryId, name);
        return list;
    }

    /**
     * 根据菜品id修改菜品状态
     * @param status
     * @param id
     */
    @Transactional
    @Override
    public void updeteByIds(Integer status, Long id) {
        if (status == 0){
            //根据菜品id查询相关套餐id
            List<Long> setMealById =  setMealDishMapper.updateSetMealById(id);
            //判断是否有菜品关联
            if (!CollectionUtils.isEmpty(setMealById)){
                //根据套餐id修改套餐状态
                setMealMapper.updateSetMealByids(setMealById);
            }
        }

        //根据菜品id修改菜品状态
        dishMapper.updateByIds(status, id);

        //清除菜品缓存
        eliminateCache("*");

    }

    //清除缓存方法
    private void eliminateCache(String cache) {
        log.info("清除菜品缓存...");
        Set keys = redisTemplate.keys("dish:cache:" + cache);
        redisTemplate.delete(keys);
    }

}
