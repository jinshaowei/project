package com.sky.service.impl.appserviceImpl;

import com.sky.entity.Category;
import com.sky.mapper.appmapper.UserCategoryMapper;
import com.sky.service.appservice.UserCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCategoryServiceImpl implements UserCategoryService {

    @Autowired
    private UserCategoryMapper userCategoryMapper;

    /**
     * 根据type查询分类
     * @param type
     * @return
     */
    @Override
    public List selectById(Integer type) {
        Category categoryById = new Category();
        categoryById.setType(type);
        categoryById.setStatus(1);

        List<Category> list =  userCategoryMapper.inquireById(categoryById);

        return list;
    }
}
