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

/*
          //    totalSensitive : 总数据条数
          //    shieldForm.size: 每页多少条数据
          //    shieldForm.current: 当前页码(当前是哪一页)
        //定义一个 当前总页数totalPage :  当前总页数等于 = 总条数减去删除一条数据  除以  每页的条数
        const totalPage = Math.ceil((this.totalSensitive -1) / this.shieldForm.size)
        shieldForm = this.shieldForm.current > totalPage ? totalPage : this.shieldForm.current;
        this.shieldForm.current = this.shieldForm.current < 1 ? 1 : this.shieldForm.current;

        假设没删除之前当前页码在最后一页, 也就是第4页

        删除后总页数就是上面定义的  totalPage  为 3

        this.shieldForm.current =  4  >  3  ?  3 : 4
*/

        List<Category> categoryList = categoryMapper.select(category);

        Page<Category> page = (Page<Category>) categoryList;

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 删除员工
    * */
    @Override
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }
}
