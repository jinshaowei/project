package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    /**
     * 新增员工
     * */
    void inst(CategoryDTO employee);

    /**
     * 分页查询
     * */
    PageResult page(CategoryPageQueryDTO category);

    /**
     * 删除分类
     * */
    void delete(Integer id);

    /**
    * 启用- 禁用
    * */
    void status(Integer status, Long id);


    /**
    * 修改分类
    * */
    void update(CategoryDTO categoryDTO);
}
