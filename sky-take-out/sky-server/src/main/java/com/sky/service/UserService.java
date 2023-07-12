package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.Category;
import com.sky.entity.User;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    /**
     * 登录请求
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

}
