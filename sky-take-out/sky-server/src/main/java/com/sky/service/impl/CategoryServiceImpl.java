package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    /**
     * 新增分类
     * */
    @Override
    public void inst(CategoryDTO categoryDTO) {
        Category category = new Category();
        //补全属性
        category.setType(categoryDTO.getType());
        category.setName(categoryDTO.getName());
        category.setSort(categoryDTO.getSort());

        //菜品状态
        category.setStatus(0);
        //创建时间
        category.setCreateTime(LocalDateTime.now());
        //修改时间
        category.setUpdateTime(LocalDateTime.now());
        //创建人ID
        category.setCreateUser(BaseContext.getCurrentId());
        //修改人ID
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.inst(category);
    }

    /**
     * 分页查询
     * */
    @Override
    public PageResult page(CategoryPageQueryDTO category) {
        //分页参数
        PageHelper.startPage(category.getPage(),category.getPageSize());

        List<Category> categoryList = categoryMapper.select(category);

        Page<Category> page = (Page<Category>) categoryList;

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 删除分类
    * */
    @Override
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }
}
