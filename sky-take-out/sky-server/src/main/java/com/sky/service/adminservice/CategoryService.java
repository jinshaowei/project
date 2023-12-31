package com.sky.service.adminservice;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

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


    /**
     * 根据id查询分类
     * */
    List<Category> select(Integer type);

}
