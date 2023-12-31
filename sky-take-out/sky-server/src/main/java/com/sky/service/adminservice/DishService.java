package com.sky.service.adminservice;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DishService {

    /**
     * 新增菜品
     * */
    void inst(DishDTO dto);



    /**
     * 分页查询
     * */
    PageResult select(DishPageQueryDTO pageQueryDTO);


    /**
     * 删除菜品
     * */
    void delete(List<Long> ids);

    /**
     * 查询数据回显
     * */
    DishVO selectId(Long id);


    /**
     * 修改数据
     * */
    void update(DishVO dishVO);

    /**
     * 根据分类id查询菜品
     * */
    List selectCategoryDsihByIds(Long categoryId, String name);

    /**
     * 根据菜品id修改菜品状态
     * @param status
     * @param id
     */
    void updeteByIds(Integer status, Long id);


}
