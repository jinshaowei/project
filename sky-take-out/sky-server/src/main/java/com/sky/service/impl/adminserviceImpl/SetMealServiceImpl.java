package com.sky.service.impl.adminserviceImpl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.mapper.adminmapper.DishMapper;
import com.sky.mapper.adminmapper.SetMealDishMapper;
import com.sky.mapper.adminmapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.adminservice.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @CacheEvict(cacheNames = "setmeal_cache", key = "#a0.categoryId")
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
        List<SetmealVO> setmealList = setMealMapper.selectSetmealPage(setmealPageQueryDTO);

        //封装查询结果
        Page<SetmealVO> page = (Page<SetmealVO>) setmealList;

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 套餐起售-停售
     * @param status
     */
    @CacheEvict(cacheNames = "setmeal_cache", allEntries = true)
    @Transactional
    @Override
    public void updateStatusById(Integer status, Long id) {
        //根据套餐id查询关联菜品
        List<Long> dishIds = setMealDishMapper.selectSetMealDishByIds(id);
        //根据菜品id查询未起售菜品
        List<Dish> dishList = dishMapper.selectSetMealDishByIds(dishIds);
        //判断集合是否为空
        if (dishList.size() != 0){
           throw  new BaseException(MessageConstant.SETMEAL_ENABLE_FAILED);
        }
        setMealMapper.updateStatusByIds(status,id);


    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @CacheEvict(cacheNames = "setmeal_cache", allEntries = true)
    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        //统计出起售套餐
        Long count = setMealMapper.selectByIds(ids);
        //判断是否有起售套餐
        if (count != 0){
            throw new BaseException(MessageConstant.SETMEAL_ON_SALE);
        }
        //批量删除套餐
        setMealMapper.deleteByIds(ids);



    }

    /**
     * 根据id查询套餐和菜品回显
     * @param id
     * @return
     */
    @Transactional
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
     * 修改套餐
     * @param setmealVO
     * @return
     */
    @CacheEvict(cacheNames = "setmeal_cache", allEntries = true)
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

//    //清除缓存方法
//    private void eliminateCache(String cache) {
//        log.info("清除套餐缓存...");
//        Set keys = redisTemplate.keys("setmeal:" + cache);
//
//        redisTemplate.delete(keys);
//    }


}
