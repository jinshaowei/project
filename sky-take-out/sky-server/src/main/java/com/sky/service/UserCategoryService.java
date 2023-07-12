package com.sky.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserCategoryService {


    /**
     * 根据id查询分类
     * @param type
     * @return
     */
    List selectById(Integer type);

}
