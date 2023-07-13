package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Slf4j
public class SetMealServiceImpl implements SetMealService{

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void insertSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();

        setmeal.setId(setmealDTO.getId());
        setmeal.setCategoryId(setmealDTO.getCategoryId());
        setmeal.setName(setmealDTO.getName());
        setmeal.setPrice(setmealDTO.getPrice());
        setmeal.setStatus(setmealDTO.getStatus());
        setmeal.setDescription(setmealDTO.getDescription());
        setmeal.setImage(setmealDTO.getImage());

        log.info("查询套餐：{}", setmeal);
        //添加套餐
       setMealMapper.insertSetMeal(setmeal);

       //添加套餐和菜品关系表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
            setMealDishMapper.insertSetmealDish(setmealDish);
        });


    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult selectPageSetmeal(SetmealPageQueryDTO setmealPageQueryDTO) {
        //获取总记录数，和当前页码
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        //将结果存储到list中
        List<Setmeal> setmealList = setMealMapper.selectSetmealPage(setmealPageQueryDTO);

        //封装查询结果
        Page<Setmeal> page = (Page<Setmeal>) setmealList;

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 套餐起售-停售
     * @param status
     */
    @Override
    public void updataStatusById(Integer status, Long id) {
        setMealMapper.updateStatusByIds(status,id);
    }

    /**
     * 批量输出套餐
     * @param ids
     */
    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        //统计出起售套餐
        Long count = setMealMapper.selectByIds(ids);
        //判断是否有起售套餐
        if (count != 0){
            throw new BaseException(MessageConstant.DISH_ON_SALE);
        }
        //批量删除套餐
        setMealMapper.deleteByIds(ids);
    }

    /**
     * 根据id查询套餐和菜品回显
     * @param id
     * @return
     */
    @Override
    public SetmealVO selectSetMalByIds(Long id) {
        //根据id查询返回结果
        SetmealVO setmealVO = setMealMapper.selectById(id);
        //获取返回的主键id
        Long ids = setmealVO.getId();
        //根据主键id查询套餐菜品关系表
        setmealVO.setSetmealDishes(setMealDishMapper.selectSetMealDish(ids));
        return setmealVO;
    }

    /**
     * 修改数据
     * @param setmealVO
     * @return
     */
    @Transactional
    @Override
    public void updateSetMeal(SetmealVO setmealVO) {
        Setmeal setmeal = new Setmeal();

        BeanUtils.copyProperties(setmealVO,setmeal);

        //修改套餐表数据
        setMealMapper.updateSetMeal(setmeal);
        System.out.println(setmeal);

        //根据主键套餐id删除关联的菜品
        setMealDishMapper.deleteSetMealDishById(setmeal.getId());

        //再添加菜品
        List<SetmealDish> setmealDishes = setmealVO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
           setmealDish.setSetmealId(setmeal.getId());
        });
        setMealDishMapper.insertSetmealDishById(setmealDishes);

    }

}
