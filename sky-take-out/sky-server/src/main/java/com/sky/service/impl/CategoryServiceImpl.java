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
        //查询结果封装成List对象
        List<Category> categoryList = categoryMapper.select(category);
        //将集合转换成Page对象
        Page<Category> page = (Page<Category>) categoryList;
        //调用Page对象中的Result方法返回一个list集合
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 删除分类
    * */
    @Override
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }

    /**
     * 启用 - 禁用
     * */
    @Override
    public void status(Integer status, Long id) {
        Category category = new Category();
        category.setStatus(status);
        category.setId(id);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.status(category);
    }


    /**
     * 修改分类
     * */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setType(categoryDTO.getType());
        category.setName(categoryDTO.getName());
        category.setSort(categoryDTO.getSort());
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);
    }
}
